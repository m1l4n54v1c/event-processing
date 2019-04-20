package io.axoniq.demo.dep1;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.serialization.Serializer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class DistributedEventProcessingApp1 {

    public static void main(String[] args) {
        new SpringApplication(DistributedEventProcessingApp1.class).run(args);
    }

    @Bean
    public ApplicationRunner applicationRunner(EventBus eventBus, Configuration configuration) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                String event = i + "-" + UUID.randomUUID().toString();
                eventBus.publish(GenericEventMessage.asEventMessage(event));
            }

            configuration.shutdown();
        };
    }

    @Bean
    public TokenStore tokenStore(EntityManagerProvider entityManagerProvider, Serializer serializer) {
        return JpaTokenStore.builder()
                            .entityManagerProvider(entityManagerProvider)
                            .serializer(serializer)
                            .build();
    }
}
