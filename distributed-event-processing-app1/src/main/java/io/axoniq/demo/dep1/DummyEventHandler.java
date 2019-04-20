package io.axoniq.demo.dep1;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Milan Savic
 */
@Component
@ProcessingGroup("dep")
public class DummyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyEventHandler.class);

    @EventHandler
    public void on(String evt) {
        LOGGER.info("DEP1 - Handling {}... {}.", evt, Thread.currentThread());
    }
}
