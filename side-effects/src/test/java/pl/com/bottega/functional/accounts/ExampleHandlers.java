package pl.com.bottega.functional.accounts;

import reactor.core.publisher.Mono;

class ExampleHandlers {
    interface IFirstCommandHandler extends Handler<FirstCommand> {

    }

    static class FirstCommandHandlerComplex implements IFirstCommandHandler {

        @Override
        public Mono<Void> handle(FirstCommand command) {
            return Mono.empty();
        }
    }

    static class FirstCommand implements Handler.Command {

    }

    static class SecondCommand implements Handler.Command {

    }

    static class FirstCommandHandler implements Handler<FirstCommand> {

        FirstCommand handledCommand;

        @Override
        public Mono<Void> handle(FirstCommand command) {
            this.handledCommand = command;
            return Mono.empty();
        }
    }

    static class SecondCommandHandler implements Handler<SecondCommand> {

        SecondCommand handledCommand;

        @Override
        public Mono<Void> handle(SecondCommand command) {
            this.handledCommand = command;
            return Mono.empty();
        }
    }

    static class SampleHandlerDecorator<CommandType extends Handler.Command> extends HandlerDecorator<CommandType> {

        public SampleHandlerDecorator(Handler<CommandType> decorated) {
            super(decorated);
        }

        @Override
        public Mono<Void> handle(CommandType command) {
            return decorated.handle(command);
        }
    }
}
