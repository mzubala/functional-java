package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static pl.com.bottega.functional.accounts.TransferFundsHandler.TransferCommand;

interface TransferFundsHandler extends Handler<TransferCommand> {

    void handle(TransferCommand command);

    @Value
    class TransferCommand implements Command {
        @NonNull
        AccountNumber source;

        @NonNull
        AccountNumber destination;

        @NonNull
        Money amount;
    }

}

@AllArgsConstructor
class DefaultTransferFundsHandler implements TransferFundsHandler {

    private final AccountRepository accountRepository;

    @Override
    // TODO write this method using reactive stream comming from repository with block at the end
    public void handle(TransferCommand command) {
        Mono.zip(
                accountRepository.find(command.getSource()),
                accountRepository.find(command.getDestination())
            )
            .flatMapMany((tuple) -> makeTransfer(command, tuple))
            .cast(Account.class)
            .map(accountRepository::save)
            .then()
            .block();
    }

    private static Flux<Account> makeTransfer(TransferCommand command, Tuple2<Account, Account> tuple) {
        final var sourceAccount = tuple.getT1();
        final var targetAccount = tuple.getT2();
        return Flux.just(
            sourceAccount.debit(command.getAmount()).get(),
            targetAccount.credit(command.getAmount()).get()
        );
    }
}
