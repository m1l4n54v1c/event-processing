package io.axoniq.demo.tep;

import io.axoniq.demo.tep.model.DummyAggregateCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Milan Savic
 */
@Component
@ProcessingGroup("tep")
public class DummyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyEventHandler.class);

    @EventHandler
    public void on(DummyAggregateCreatedEvent evt) {
        LOGGER.info("Handling {}... {}.", evt, Thread.currentThread());
    }

    @ResetHandler
    public void onReset() {
        LOGGER.info("Resetting...");
    }
}
