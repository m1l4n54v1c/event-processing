package io.axoniq.demo.sep.model;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author Milan Savic
 */
@Value
public class EchoCommand {

    @TargetAggregateIdentifier
    private final String aggregateId;
    private final String echo;
}
