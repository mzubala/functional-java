package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static pl.com.bottega.functional.accounts.TransferFundsHandler.TransferCommand;

interface TransferFundsHandler extends Handler<TransferCommand> {

    Mono<Void> handle(TransferCommand command);

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
// TODO implement new handler interface
class DefaultTransferFundsHandler implements TransferFundsHandler {

    private final AccountRepository accountRepository;

    @Override
    public void handle(TransferCommand command) {
        Mono.zip(
                accountRepository.find(command.getSource()),
                accountRepository.find(command.getDestination())
            ).flatMapMany((accounts) ->
                Flux.fromIterable(accounts.mapT1(
                    sourceAccount -> sourceAccount.debit(command.getAmount()).get()
                ).mapT2(
                    targetAccount -> targetAccount.credit(command.getAmount()).get()
                ))
            ).cast(Account.class)
            .flatMap(accountRepository::save)
            .then().block();
    }
}
