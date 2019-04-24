package io.axoniq.demo.pep;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Milan Savic
 */
@RestController
public class ParallelEventProcessingController {

    private final EventBus eventBus;

    public ParallelEventProcessingController(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @PostMapping("/run/{parallelism}")
    public void run(@PathVariable int parallelism) {
        for (int i = 0; i < parallelism; i++) {
            String eventId = i + "-" + UUID.randomUUID().toString();
            eventBus.publish(GenericEventMessage.asEventMessage(new MyEvent(eventId)));
        }
    }
}
