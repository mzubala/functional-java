package pl.com.bottega.functional.accounts;

import org.springframework.transaction.reactive.TransactionalOperator;
import pl.com.bottega.functional.accounts.Handler.Command;
import reactor.core.publisher.Mono;

class TransactionalHandler<CommandType extends Command> extends HandlerDecorator<CommandType> {

    private final TransactionalOperator transactionalOperator;

    public TransactionalHandler(TransactionalOperator transactionalOperator, Handler<CommandType> handler) {
        super(handler);
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Void> handle(CommandType command) {
        return transactionalOperator.execute(status -> decorated.handle(command)).then();
    }
}