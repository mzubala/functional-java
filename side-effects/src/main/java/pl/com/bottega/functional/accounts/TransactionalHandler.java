package pl.com.bottega.functional.accounts;

import org.springframework.transaction.support.TransactionTemplate;
import pl.com.bottega.functional.accounts.Handler.Command;

class TransactionalHandler<CommandType extends Command> extends HandlerDecorator<CommandType> {

    private final TransactionTemplate transactionTemplate;

    public TransactionalHandler(TransactionTemplate transactionTemplate, Handler<CommandType> handler) {
        super(handler);
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void handle(CommandType command) {
        transactionTemplate.executeWithoutResult((status) -> decorated.handle(command));
    }
}
