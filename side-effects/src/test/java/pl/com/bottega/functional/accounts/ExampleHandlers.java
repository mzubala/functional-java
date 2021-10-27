package pl.com.bottega.functional.accounts;

class ExampleHandlers {
    interface IFirstCommandHandler extends Handler<FirstCommand> {

    }

    static class FirstCommandHandlerComplex implements IFirstCommandHandler {

        @Override
        public void handle(FirstCommand command) {
        }
    }

    static class FirstCommand implements Handler.Command {

    }

    static class SecondCommand implements Handler.Command {

    }

    static class FirstCommandHandler implements Handler<FirstCommand> {

        FirstCommand handledCommand;

        @Override
        public void handle(FirstCommand command) {
            this.handledCommand = command;
        }
    }

    static class SecondCommandHandler implements Handler<SecondCommand> {

        SecondCommand handledCommand;

        @Override
        public void handle(SecondCommand command) {
            this.handledCommand = command;
        }
    }

    static class SampleHandlerDecorator<CommandType extends Handler.Command> extends HandlerDecorator<CommandType> {

        public SampleHandlerDecorator(Handler<CommandType> decorated) {
            super(decorated);
        }

        @Override
        public void handle(CommandType command) {
            decorated.handle(command);
        }
    }
}
