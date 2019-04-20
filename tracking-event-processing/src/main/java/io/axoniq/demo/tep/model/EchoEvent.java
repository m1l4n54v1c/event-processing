package io.axoniq.demo.tep.model;

import lombok.Value;

/**
 * @author Milan Savic
 */
@Value
public class EchoEvent {

    private final String aggregateId;
    private final String echo;
}
