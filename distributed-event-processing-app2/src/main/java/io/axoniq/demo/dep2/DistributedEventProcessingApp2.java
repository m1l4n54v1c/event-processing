package io.axoniq.demo.dep2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class DistributedEventProcessingApp2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedEventProcessingApp2.class);

    public static void main(String[] args) {
        new SpringApplication(DistributedEventProcessingApp2.class).run(args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            LOGGER.info("Waiting for events...");
            while(true) ;
        };
    }
}
