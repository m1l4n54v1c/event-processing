package io.axoniq.demo.pep;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Milan Savic
 */
@Component
@ProcessingGroup("pep")
public class DummyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyEventHandler.class);

    @EventHandler
    public void on(MyEvent evt) {
        LOGGER.info("Handling {}... {}.", evt, Thread.currentThread());
    }
}
