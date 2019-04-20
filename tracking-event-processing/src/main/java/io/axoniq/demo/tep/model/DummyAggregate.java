package io.axoniq.demo.tep.model;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * @author Milan Savic
 */
@SuppressWarnings("unused")
@Aggregate
public class DummyAggregate {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyAggregate.class);

    @AggregateIdentifier
    private String id;
    private String echo;

    public DummyAggregate() {
    }

    @CommandHandler
    public DummyAggregate(CreateDummyAggregateCommand cmd) {
        LOGGER.info("Received command {}. {}.", cmd, Thread.currentThread());
        apply(new DummyAggregateCreatedEvent(cmd.getAggregateId()));
    }

    @EventSourcingHandler
    public void on(DummyAggregateCreatedEvent evt) {
        this.id = evt.getAggregateId();
        LOGGER.info("Event applied {}. {}.", evt, Thread.currentThread());
    }

    @CommandHandler
    public void handle(EchoCommand cmd) {
        LOGGER.info("Received command {}. {}.", cmd, Thread.currentThread());
        apply(new EchoEvent(cmd.getAggregateId(), cmd.getEcho()));
    }

    @EventSourcingHandler
    public void on(EchoEvent evt) {
        this.echo = evt.getEcho();
        LOGGER.info("Event applied {}. {}.", evt, Thread.currentThread());
    }
}
