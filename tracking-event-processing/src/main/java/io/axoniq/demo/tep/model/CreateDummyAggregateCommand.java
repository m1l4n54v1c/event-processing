package io.axoniq.demo.tep.model;

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
