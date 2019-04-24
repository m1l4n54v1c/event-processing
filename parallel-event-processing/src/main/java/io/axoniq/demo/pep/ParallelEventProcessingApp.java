package io.axoniq.demo.pep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class ParallelEventProcessingApp {

    public static void main(String[] args) {
        new SpringApplication(ParallelEventProcessingApp.class).run(args);
    }
}
