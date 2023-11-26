package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Value;
import reactor.core.publisher.Mono;

import static pl.com.bottega.functional.accounts.DepositFundsHandler.DepositFundsCommand;
import static pl.com.bottega.functional.accounts.TryHelper.toMono;

interface DepositFundsHandler extends Handler<DepositFundsCommand> {
    Mono<Void> handle(DepositFundsCommand command);

    @Value
    class DepositFundsCommand implements Command {
        AccountNumber destination;
        Money amount;
    }
}

@AllArgsConstructor
class DefaultDepositFundsHandler implements DepositFundsHandler {

    private final AccountRepository accountRepository;

    @Override
    public Mono<Void> handle(DepositFundsCommand command) {
        return accountRepository.find(command.getDestination())
            .flatMap((account) -> toMono(account.credit(command.getAmount())))
            .flatMap(accountRepository::save);
    }
}
