package io.axoniq.demo.sep;

import io.axoniq.demo.sep.model.CreateDummyAggregateCommand;
import io.axoniq.demo.sep.model.EchoCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Milan Savic
 */
@RestController
public class SubscribingEventProcessingController {

    private final CommandGateway commandGateway;

    public SubscribingEventProcessingController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/create")
    public String create() {
        return commandGateway.sendAndWait(new CreateDummyAggregateCommand(UUID.randomUUID().toString()));
    }

    @PostMapping("/echo/{id}")
    public void echo(@PathVariable String id, @RequestBody String echo) {
        commandGateway.sendAndWait(new EchoCommand(id, echo));
    }
}
