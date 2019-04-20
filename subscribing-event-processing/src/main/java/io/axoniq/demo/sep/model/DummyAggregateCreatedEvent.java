package io.axoniq.demo.sep.model;

import lombok.Value;

/**
 * @author Milan Savic
 */
@Value
public class DummyAggregateCreatedEvent {

    private final String aggregateId;
}
