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
    public void handle(TransferCommand command) {
        /*Mono.zip(
                accountRepository.find(command.getSource()),
                accountRepository.find(command.getDestination())
        ).flatMap((accounts) -> {
            var saved = accounts
                    .mapT1(source -> accountRepository.save(source.debit(command.getAmount()).get()))
                    .mapT2(dest -> accountRepository.save(dest.credit(command.getAmount()).get()));
            return saved.getT1().zipWith(saved.getT2());
        }).block();*/
        /**
         var amount = command.getAmount();

         var debit = accountRepository.find(command.getSource())
         .map(acc -> acc.debit(amount));


         var credit = accountRepository.find(command.getDestination())
         .map(acc -> acc.credit(amount));


         debit.mergeWith(credit)
         .map(Try::get)
         .flatMap(accountRepository::save)
         .blockLast();
         */
        Mono.zip(
                accountRepository.find(command.getSource()),
                accountRepository.find(command.getDestination())
        ).flatMapMany((accounts) -> Flux.just(
                accounts.getT1().debit(command.getAmount()).get(),
                accounts.getT2().credit(command.getAmount()).get()
        )).flatMap(accountRepository::save).blockLast();
    }
}
