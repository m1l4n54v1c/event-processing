package io.axoniq.demo.tep;

import io.axoniq.demo.tep.model.CreateDummyAggregateCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.EventTrackerStatus;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * @author Milan Savic
 */
@RestController
public class TrackingEventProcessingWeb {

    private final CommandGateway commandGateway;
    private final EventProcessingConfiguration eventProcessingConfiguration;

    public TrackingEventProcessingWeb(CommandGateway commandGateway,
                                      EventProcessingConfiguration eventProcessingConfiguration) {
        this.commandGateway = commandGateway;
        this.eventProcessingConfiguration = eventProcessingConfiguration;
    }

    @PostMapping("/create")
    public Future<String> create() {
        return commandGateway.send(new CreateDummyAggregateCommand(UUID.randomUUID().toString()));
    }

    @PostMapping("/replay")
    public void replay() {
        eventProcessingConfiguration.eventProcessor("tep", TrackingEventProcessor.class)
                                    .ifPresent(tep -> {
                                        tep.shutDown();
                                        tep.resetTokens();
                                        tep.start();
                                    });
    }

    @GetMapping("/status")
    public Map<Integer, EventTrackerStatus> status() {
        return eventProcessingConfiguration.eventProcessor("tep", TrackingEventProcessor.class)
                                           .map(TrackingEventProcessor::processingStatus)
                                           .orElseThrow(IllegalStateException::new);
    }
}
