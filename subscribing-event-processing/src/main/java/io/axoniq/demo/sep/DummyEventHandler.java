package io.axoniq.demo.sep;

import io.axoniq.demo.sep.model.DummyAggregateCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Milan Savic
 */
@ProcessingGroup("sep")
@Component
public class DummyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyEventHandler.class);

    @EventHandler
    public void on(DummyAggregateCreatedEvent evt) throws Exception {
        LOGGER.info("Handling {}... {}. ", evt, Thread.currentThread());
//        throw new Exception("oops");
    }
}
