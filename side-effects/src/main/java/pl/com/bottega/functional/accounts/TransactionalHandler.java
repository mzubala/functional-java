package pl.com.bottega.functional.accounts;

import org.springframework.transaction.reactive.TransactionalOperator;
import pl.com.bottega.functional.accounts.Handler.Command;

// TODO implement new handler interface
class TransactionalHandler<CommandType extends Command> extends HandlerDecorator<CommandType> {

    private final TransactionalOperator transactionalOperator;

    public TransactionalHandler(TransactionalOperator transactionalOperator, Handler<CommandType> handler) {
        super(handler);
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public void handle(CommandType command) {
        // TODO use transctionalOperator to ensure transaction
        decorated.handle(command);
    }
}
