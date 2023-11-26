package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static pl.com.bottega.functional.accounts.TransferFundsHandler.TransferCommand;
import static pl.com.bottega.functional.accounts.TryHelper.toMono;

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
class DefaultTransferFundsHandler implements TransferFundsHandler {

    private final AccountRepository accountRepository;

    @Override
    public Mono<Void> handle(TransferCommand command) {
        return Mono.zip(
                accountRepository.find(command.getSource()),
                accountRepository.find(command.getDestination())
        ).flatMapMany((accounts) -> {
            var changedAccounts = accounts
                    .mapT1(source -> toMono(source.debit(command.getAmount())))
                    .mapT2(dest -> toMono(dest.credit(command.getAmount())));
            return Flux.merge(changedAccounts.getT1(), changedAccounts.getT2());
        }).flatMap(accountRepository::save).then();
    }
}
