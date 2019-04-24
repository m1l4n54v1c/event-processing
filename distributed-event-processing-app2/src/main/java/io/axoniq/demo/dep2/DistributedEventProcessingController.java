package io.axoniq.demo.dep2;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.EventTrackerStatus;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Milan Savic
 */
@RestController
public class DistributedEventProcessingController {

    private final EventProcessingConfiguration eventProcessingConfiguration;

    public DistributedEventProcessingController(
            EventProcessingConfiguration eventProcessingConfiguration) {
        this.eventProcessingConfiguration = eventProcessingConfiguration;
    }

    @GetMapping("/status")
    public Map<Integer, EventTrackerStatus> status() {
        return eventProcessingConfiguration.eventProcessor("dep", TrackingEventProcessor.class)
                                           .map(TrackingEventProcessor::processingStatus)
                                           .orElseThrow(IllegalStateException::new);
    }
}
