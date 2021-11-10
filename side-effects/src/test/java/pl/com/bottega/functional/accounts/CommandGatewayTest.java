package pl.com.bottega.functional.accounts;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandGatewayTest {

    private final ExampleHandlers.FirstCommandHandler firstCommandHandler = new ExampleHandlers.FirstCommandHandler();

    private final ExampleHandlers.SecondCommandHandler secondCommandHandler = new ExampleHandlers.SecondCommandHandler();

    private final ExampleHandlers.FirstCommand firstCommand = new ExampleHandlers.FirstCommand();

    private final ExampleHandlers.SecondCommand secondCommand = new ExampleHandlers.SecondCommand();

    @Test
    public void passesCommandsToCorrectHandlers() {
        // given
        var gateway = new CommandGateway(list(firstCommandHandler, secondCommandHandler));

        // when
        gateway.execute(firstCommand);

        // then
        assertEquals(firstCommand, firstCommandHandler.handledCommand);
        assertNull(secondCommandHandler.handledCommand);

        // when
        gateway.execute(secondCommand);

        // then
        assertEquals(secondCommand, secondCommandHandler.handledCommand);
        assertEquals(firstCommand, firstCommandHandler.handledCommand);
    }

    @Test
    public void throwsErrorWhenHandlerIsNotFound() {
        // given
        var gateway = new CommandGateway(list(secondCommandHandler));

        // when
        assertThrows(IllegalArgumentException.class, () -> gateway.execute(firstCommand));
    }

    @Test
    public void forbidsAnonymousHandlers() {
        // given
        Handler<ExampleHandlers.FirstCommand> anonymousHandler = command -> Mono.fromCallable(() -> {
            System.out.println("");
            return null;
        });

        // expect
        assertThrows(IllegalArgumentException.class, () -> new CommandGateway(list(anonymousHandler)));
    }

    @Test
    public void allowsDecoratedHandlers() {
        // given
        var gateway = new CommandGateway(list(new ExampleHandlers.SampleHandlerDecorator<>(firstCommandHandler)));

        // when
        gateway.execute(firstCommand);

        // then
        assertEquals(firstCommand, firstCommandHandler.handledCommand);
    }
}