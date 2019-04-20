package io.axoniq.demo.pep;

import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class ParallelEventProcessingApp {

    public static void main(String[] args) {
        new SpringApplication(ParallelEventProcessingApp.class).run(args);
    }

    @Bean
    public ApplicationRunner applicationRunner(EventBus eventBus, Configuration configuration) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                String eventId = i + "-" + UUID.randomUUID().toString();
                eventBus.publish(GenericEventMessage.asEventMessage(new MyEvent(eventId)));
            }

            configuration.shutdown();
        };
    }
}
