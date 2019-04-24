package io.axoniq.demo.dep1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class DistributedEventProcessingApp1 {

    public static void main(String[] args) {
        new SpringApplication(DistributedEventProcessingApp1.class).run(args);
    }
}
