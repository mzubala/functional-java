package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

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
        final var sourceAccount = accountRepository.find(command.getSource());
        final var targetAccount = accountRepository.find(command.getDestination());
        sourceAccount.debit(command.getAmount());
        targetAccount.credit(command.getAmount());
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
    }
}
