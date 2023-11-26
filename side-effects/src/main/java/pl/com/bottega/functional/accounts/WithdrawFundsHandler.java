package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Value;
import reactor.core.publisher.Mono;

import static pl.com.bottega.functional.accounts.WithdrawFundsHandler.*;

interface WithdrawFundsHandler extends Handler<WithdrawFundsCommand> {
    Mono<Void> handle(WithdrawFundsCommand command);

    @Value
    class WithdrawFundsCommand implements Command {
        AccountNumber destination;
        Money amount;
    }
}

@AllArgsConstructor
// TODO implement new interface
class DefaultWithdrawFundsHandler implements WithdrawFundsHandler {

    private final AccountRepository accountRepository;

    @Override
    public void handle(WithdrawFundsCommand command) {
        accountRepository.find(command.getDestination())
            .map(account -> account.debit(command.getAmount()).get())
            .flatMap(accountRepository::save).block();
    }
}
