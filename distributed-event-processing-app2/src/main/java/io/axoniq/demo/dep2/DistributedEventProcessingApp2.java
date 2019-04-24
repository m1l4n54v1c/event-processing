package io.axoniq.demo.dep2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class DistributedEventProcessingApp2 {

    public static void main(String[] args) {
        new SpringApplication(DistributedEventProcessingApp2.class).run(args);
    }
}
