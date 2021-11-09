package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Value;

import static pl.com.bottega.functional.accounts.WithdrawFundsHandler.*;

interface WithdrawFundsHandler extends Handler<WithdrawFundsCommand> {
    void handle(WithdrawFundsCommand command);

    @Value
    class WithdrawFundsCommand implements Command {
        AccountNumber destination;
        Money amount;
    }
}

@AllArgsConstructor
class DefaultWithdrawFundsHandler implements WithdrawFundsHandler {

    private final AccountRepository accountRepository;

    @Override
    public void handle(WithdrawFundsCommand command) {
        var account = accountRepository.find(command.getDestination());
        account.debit(command.getAmount()).get();
        accountRepository.save(account);
    }
}
