package pl.com.bottega.functional.accounts;

import io.vavr.control.Try;
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

    public Try<?> debit(Money amount) {
        return balance.isLessThan(amount).flatMap((insufficientFunds) -> {
            if(insufficientFunds) {
                return Try.failure(new InsufficientFundsException());
            }
            return balance.subtract(amount);
        }).map(newBalance -> {
            this.balance = newBalance;
            return null;
        });
    }

    public Try<?> credit(Money amount) {
        return balance.add(amount).map((newBalance) -> {
            this.balance = newBalance;
            return null;
        });
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
