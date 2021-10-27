package pl.com.bottega.functional.accounts;

import lombok.NonNull;
import lombok.Value;

import java.util.Currency;

class Account {
    private final CustomerId customerId;
    private final AccountNumber accountNumber;

    private Money balance;

    private final Long version;

    public Account(CustomerId customerId, AccountNumber accountNumber, Currency currency) {
        this(customerId, accountNumber, Money.zero(currency), null);
    }

    public Account(CustomerId customerId, AccountNumber accountNumber, Money balance, Long version) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.version = version;
    }

    public void debit(Money amount) {
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException();
        }
        balance = balance.subtract(amount);
    }

    public void credit(Money amount) {
        balance = balance.add(amount);
    }

    public AccountNumber getNumber() {
        return accountNumber;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getBalance() {
        return balance;
    }

    public Long getVersion() {
        return version;
    }
}

@Value
class AccountNumber {
    @NonNull
    String value;
}
