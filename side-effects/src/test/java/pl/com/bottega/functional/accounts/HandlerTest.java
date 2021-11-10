package pl.com.bottega.functional.accounts;

import org.junit.jupiter.api.Test;
import pl.com.bottega.functional.accounts.ExampleHandlers.FirstCommand;
import pl.com.bottega.functional.accounts.ExampleHandlers.FirstCommandHandler;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerTest {
    @Test
    public void returnsSupportedCommandTypeForAnonymousHandler() {
        // given
        var handler = new Handler<>() {
            @Override
            public Mono<Void> handle(Command command) {
                return Mono.empty();
            }
        };

        // expect
        assertThat(handler.supportedCommandType()).map(Type::getTypeName).contains(Handler.Command.class.getTypeName());
    }

    @Test
    public void returnsNoSupportedCommandTypeForLambdaDefinedHandler() {
        // given
        Handler<FirstCommand> anonymousHandler = command -> Mono.fromCallable(() -> {
            System.out.println("");
            return null;
        });

        // expect
        assertThat(anonymousHandler.supportedCommandType()).isEmpty();
    }

    @Test
    public void returnsCommandTypeForAClassImplementingHandlerDirectly() {
        // given
        var handler = new FirstCommandHandler();

        // expect
        assertThat(handler.supportedCommandType().map(Type::getTypeName)).contains(FirstCommand.class.getTypeName());
    }

    @Test
    public void returnsCommandTypeForHandlerImplementingInterfaceExtendingHandler() {
        // given
        var handler = new ExampleHandlers.FirstCommandHandlerComplex();

        // expect
        assertThat(handler.supportedCommandType()).map(Type::getTypeName).contains(FirstCommand.class.getTypeName());
    }
}
