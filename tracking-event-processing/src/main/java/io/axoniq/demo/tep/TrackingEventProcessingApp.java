package io.axoniq.demo.tep;

import io.axoniq.demo.tep.model.CreateDummyAggregateCommand;
import io.axoniq.demo.tep.model.EchoCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.config.Configuration;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.TrackingToken;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.messaging.StreamableMessageSource;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class TrackingEventProcessingApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingEventProcessingApp.class);

    public static void main(String[] args) {
        new SpringApplication(TrackingEventProcessingApp.class).run(args);
    }

    @Bean
    public ApplicationRunner applicationRunner(Configuration configuration, CommandGateway commandGateway,
                                               DummyEventHandler dummyEventHandler,
                                               StreamableMessageSource<TrackedEventMessage<?>> streamableMessageSource) {
        return args -> {
            String aggregateId = UUID.randomUUID().toString();

            dispatch(commandGateway, new CreateDummyAggregateCommand(aggregateId));
            dispatch(commandGateway, new EchoCommand(aggregateId, "Hi from TEP!"));

            startOver(configuration.eventProcessingConfiguration());
            TrackingToken headToken = streamableMessageSource.createHeadToken();
            while (!dummyEventHandler.lastConsumedEventToken().covers(headToken)) {
                LockSupport.parkNanos(10);
            }
            configuration.shutdown();
        };
    }

    private void dispatch(CommandGateway commandGateway, Object cmd) {
        try {
            LOGGER.info("Dispatching command {}...", cmd);
            commandGateway.sendAndWait(cmd);
            LOGGER.info("Command {} handled successfully.", cmd);
        } catch (Exception ex) {
            LOGGER.error("Error happened while processing command {}.", cmd, ex);
        }
    }

    @Bean
    public ListenerInvocationErrorHandler listenerInvocationErrorHandler() {
        return PropagatingErrorHandler.INSTANCE;
    }

    @Bean
    public TokenStore tokenStore(EntityManagerProvider entityManagerProvider, Serializer serializer) {
        return JpaTokenStore.builder()
                            .entityManagerProvider(entityManagerProvider)
                            .serializer(serializer)
                            .build();
    }

    private void startOver(EventProcessingConfiguration configuration) {
        LOGGER.info("Resetting tep");
        configuration.eventProcessor("tep", TrackingEventProcessor.class)
                     .ifPresent(tep -> {
                         waitForThreadToRampUp(tep);
                         tep.shutDown();
                         tep.resetTokens();
                         tep.start();
                         waitForThreadToRampUp(tep);
                     });
    }

    private void waitForThreadToRampUp(TrackingEventProcessor tep) {
        while (tep.activeProcessorThreads() == 0) {
            // wait processor to ramp up
            LockSupport.parkNanos(10);
        }
    }
}
