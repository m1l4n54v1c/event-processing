package io.axoniq.demo.sep;

import io.axoniq.demo.sep.model.CreateDummyAggregateCommand;
import io.axoniq.demo.sep.model.EchoCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.eventhandling.SubscribingEventProcessor;
import org.axonframework.eventhandling.async.AsynchronousEventProcessingStrategy;
import org.axonframework.eventhandling.async.SequentialPerAggregatePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class SubscribingEventProcessingApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribingEventProcessingApp.class);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        new SpringApplication(SubscribingEventProcessingApp.class).run(args);
    }

    @Bean
    public ApplicationRunner applicationRunner(CommandGateway commandGateway, Configuration configuration) {
        return args -> {
            String aggregateId = UUID.randomUUID().toString();
            dispatch(commandGateway, new CreateDummyAggregateCommand(aggregateId));
            dispatch(commandGateway, new EchoCommand(aggregateId, "Hi from SEP!"));

            configuration.shutdown();
            executor.shutdown();
        };
    }

    private static void dispatch(CommandGateway commandGateway, Object cmd) {
        try {
            LOGGER.info("Dispatching command {}...", cmd);
            commandGateway.sendAndWait(cmd);
            LOGGER.info("Command {} handled successfully.", cmd);
        } catch (Exception ex) {
            LOGGER.error("Error happened while processing command {}.", cmd, ex);
        }
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.usingSubscribingEventProcessors();
        AsynchronousEventProcessingStrategy processingStrategy = new AsynchronousEventProcessingStrategy(
                executor,
                SequentialPerAggregatePolicy.instance());
        configurer.registerEventProcessorFactory((name, config, ehi) ->
                                                         SubscribingEventProcessor.builder()
                                                                                  .name(name)
                                                                                  .eventHandlerInvoker(ehi)
                                                                                  .messageSource(config.eventBus())
                                                                                  .processingStrategy(processingStrategy)
                                                                                  .build());
    }

    @Bean
    public ListenerInvocationErrorHandler listenerInvocationErrorHandler() {
        return PropagatingErrorHandler.INSTANCE;
    }
}
