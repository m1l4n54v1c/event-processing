package io.axoniq.demo.sep;

import io.axoniq.demo.sep.model.CreateDummyAggregateCommand;
import io.axoniq.demo.sep.model.EchoCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public void create(@RequestBody String id) {
        commandGateway.sendAndWait(new CreateDummyAggregateCommand(id));
    }

    @PostMapping("/echo/{id}")
    public void echo(@PathVariable String id, @RequestBody String echo) {
        commandGateway.sendAndWait(new EchoCommand(id, echo));
    }
}
