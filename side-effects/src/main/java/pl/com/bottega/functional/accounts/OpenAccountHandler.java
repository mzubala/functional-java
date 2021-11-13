package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Value;
import reactor.core.publisher.Mono;

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
        Mono.zip(
            customerRepository.find(command.getCustomerId()),
            accountNumberGenerator.generate()
        ).map(tuple ->
            new Account(tuple.getT1().getId(), tuple.getT2(), command.getCurrency())
        ).flatMap(accountRepository::save).block();
    }
}


