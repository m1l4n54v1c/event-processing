package io.axoniq.demo.dep1;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventTrackerStatus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * @author Milan Savic
 */
@RestController
public class DistributedEventProcessingController {

    private final EventBus eventBus;
    private final EventProcessingConfiguration eventProcessingConfiguration;

    public DistributedEventProcessingController(EventBus eventBus,
                                                EventProcessingConfiguration eventProcessingConfiguration) {
        this.eventBus = eventBus;
        this.eventProcessingConfiguration = eventProcessingConfiguration;
    }

    @PostMapping("/run/{parallelism}")
    public void run(@PathVariable int parallelism) {
        for (int i = 0; i < parallelism; i++) {
            String event = i + "-" + UUID.randomUUID().toString();
            eventBus.publish(GenericEventMessage.asEventMessage(event));
        }
    }

    @GetMapping("/status")
    public Map<Integer, EventTrackerStatus> status() {
        return eventProcessingConfiguration.eventProcessor("dep", TrackingEventProcessor.class)
                                           .map(TrackingEventProcessor::processingStatus)
                                           .orElseThrow(IllegalStateException::new);
    }
}
