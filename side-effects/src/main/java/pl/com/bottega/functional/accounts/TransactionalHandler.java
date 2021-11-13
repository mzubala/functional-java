package pl.com.bottega.functional.accounts;

import pl.com.bottega.functional.accounts.Handler.Command;

class TransactionalHandler<CommandType extends Command> extends HandlerDecorator<CommandType> {

    public TransactionalHandler(Handler<CommandType> handler) {
        super(handler);
    }

    @Override
    public void handle(CommandType command) {
        decorated.handle(command);
    }
}
