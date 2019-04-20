package io.axoniq.demo.sep.model;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author Milan Savic
 */
@Value
public class CreateDummyAggregateCommand {

    @TargetAggregateIdentifier
    private final String aggregateId;
}
