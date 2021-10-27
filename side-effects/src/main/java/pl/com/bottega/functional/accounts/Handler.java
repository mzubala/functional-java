package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static pl.com.bottega.functional.accounts.Handler.Command;

interface Handler<CommandType extends Command> {
    void handle(CommandType command);

    default Optional<Type> supportedCommandType() {
        return getGenericInterfaces(getClass())
            .filter(type -> type.getTypeName().contains(Handler.class.getTypeName()))
            .findFirst()
            .map(type -> type.getActualTypeArguments()[0]);
    }

    private Stream<ParameterizedType> getGenericInterfaces(Class<?> klass) {
        return Arrays.stream(klass.getGenericInterfaces()).flatMap(this::getParametrizedTypes);
    }

    private Stream<ParameterizedType> getParametrizedTypes(Type type) {
        if (type instanceof Class) {
            return getGenericInterfaces((Class<?>) type);
        } else {
            return Stream.of((ParameterizedType) type);
        }
    }

    interface Command {
    }
}

@AllArgsConstructor
abstract class HandlerDecorator<CommandType extends Command> implements Handler<CommandType> {

    protected final Handler<CommandType> decorated;

    @Override
    public Optional<Type> supportedCommandType() {
        return decorated.supportedCommandType();
    }
}
