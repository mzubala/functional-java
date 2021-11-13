package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Value;

import static pl.com.bottega.functional.accounts.DepositFundsHandler.DepositFundsCommand;

interface DepositFundsHandler extends Handler<DepositFundsCommand> {
    void handle(DepositFundsCommand command);

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
    // TODO write this method using reactive stream comming from repository with block at the end
    public void handle(DepositFundsCommand command) {
        var account = accountRepository.find(command.getDestination());
        account.credit(command.getAmount()).andThen(accountRepository::save).get();
    }
}
