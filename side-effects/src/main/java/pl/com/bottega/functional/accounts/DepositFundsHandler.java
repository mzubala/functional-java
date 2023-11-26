package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Value;
import reactor.core.publisher.Mono;

import static pl.com.bottega.functional.accounts.DepositFundsHandler.DepositFundsCommand;

interface DepositFundsHandler extends Handler<DepositFundsCommand> {
    Mono<Void> handle(DepositFundsCommand command);

    @Value
    class DepositFundsCommand implements Command {
        AccountNumber destination;
        Money amount;
    }
}

@AllArgsConstructor
// TODO implement new handler interface
class DefaultDepositFundsHandler implements DepositFundsHandler {

    private final AccountRepository accountRepository;

    @Override
    public void handle(DepositFundsCommand command) {
        accountRepository.find(command.getDestination())
            .map((account) -> account.credit(command.getAmount()).get())
            .flatMap(accountRepository::save).block();
    }
}
