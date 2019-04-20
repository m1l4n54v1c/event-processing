package io.axoniq.demo.tep;

import io.axoniq.demo.tep.model.DummyAggregateCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.GenericTrackedDomainEventMessage;
import org.axonframework.eventhandling.GlobalSequenceTrackingToken;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.TrackingToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Milan Savic
 */
@Component
@ProcessingGroup("tep")
public class DummyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyEventHandler.class);

    private static final TrackingToken UNKNOWN = new GlobalSequenceTrackingToken(-1);

    private final AtomicReference<TrackingToken> lastConsumedEventToken = new AtomicReference<>(UNKNOWN);

    @EventHandler
    public void on(DummyAggregateCreatedEvent evt) {
        LOGGER.info("Handling {}... {}.", evt, Thread.currentThread());
    }

    @EventHandler
    public void on(GenericTrackedDomainEventMessage<?> evt) {
        lastConsumedEventToken.set(evt.trackingToken());
    }

    @ResetHandler
    public void onReset() {
        LOGGER.info("Resetting...");
        lastConsumedEventToken.set(UNKNOWN);
    }

    public TrackingToken lastConsumedEventToken() {
        return lastConsumedEventToken.get();
    }
}
