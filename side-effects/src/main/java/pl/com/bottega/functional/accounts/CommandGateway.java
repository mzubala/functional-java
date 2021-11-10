package pl.com.bottega.functional.accounts;

import org.springframework.stereotype.Component;
import pl.com.bottega.functional.accounts.Handler.Command;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
class CommandGateway {
    private final Map<String, Handler<?>> handlersMap;

    public CommandGateway(List<Handler<?>> handlers) {
        this.handlersMap = handlers.stream().collect(
                Collectors.toMap(
                        handler -> handler
                                .supportedCommandType()
                                .map(Type::getTypeName)
                                .orElseThrow(noCommandTypeFoundFor(handler)),
                        Function.identity()
                )
        );
    }

    private Supplier<IllegalArgumentException> noCommandTypeFoundFor(Handler<?> handler) {
        return () -> new IllegalArgumentException(
                "Handler " + handler.getClass().getName() + " does not have supported command type"
        );
    }

    Mono<Void> execute(Command command) {
        Class<? extends Command> commandClass = command.getClass();
        Handler handler = handlersMap.get(commandClass.getTypeName());
        if(handler == null) {
            throw new IllegalArgumentException("No handler found for " + command.getClass().getName());
        }
        return handler.handle(command);
    }
}
