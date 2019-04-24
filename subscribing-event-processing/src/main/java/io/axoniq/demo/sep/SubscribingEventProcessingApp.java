package io.axoniq.demo.sep;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.eventhandling.SubscribingEventProcessor;
import org.axonframework.eventhandling.async.AsynchronousEventProcessingStrategy;
import org.axonframework.eventhandling.async.SequentialPerAggregatePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class SubscribingEventProcessingApp {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        new SpringApplication(SubscribingEventProcessingApp.class).run(args);
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
