package io.axoniq.demo.tep;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.serialization.Serializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class TrackingEventProcessingApp {

    public static void main(String[] args) {
        new SpringApplication(TrackingEventProcessingApp.class).run(args);
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
}
