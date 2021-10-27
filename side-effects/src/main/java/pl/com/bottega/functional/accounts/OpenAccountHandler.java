package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Currency;

import static pl.com.bottega.functional.accounts.OpenAccountHandler.*;

interface OpenAccountHandler extends Handler<OpenAccountCommand> {

    void handle(OpenAccountCommand command);

    @Value
    class OpenAccountCommand implements Command {
        CustomerId customerId;
        Currency currency;
    }
}

@AllArgsConstructor
class DefaultOpenAccountHandler implements OpenAccountHandler {

    private final CustomerRepository customerRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountRepository accountRepository;

    @Override
    public void handle(OpenAccountCommand command) {
        var customer = customerRepository.find(command.getCustomerId());
        var number = accountNumberGenerator.generate();
        var account = new Account(customer.getId(), number, command.getCurrency());
        accountRepository.save(account);
    }
}


