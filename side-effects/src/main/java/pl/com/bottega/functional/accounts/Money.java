package pl.com.bottega.functional.accounts;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Value
class Money {

    @NonNull
    BigDecimal value;

    @NonNull
    Currency currency;

    static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    Try<Money> add(Money other) {
        return null;
    }

    Try<Money> subtract(Money other) {
        return null;
    }

    Try<Boolean> isGreaterThan(Money other) {
        return null;
    }

    Try<Boolean> isLessThan(Money other) {
        return null;
    }

    Try<Boolean> isGreaterThanOrEqualTo(Money other) {
        return null;
    }

    Try<Boolean> isLessThanOrEqualTo(Money other) {
        return null;
    }
}

class IncompatibleCurrenciesException extends RuntimeException {
    IncompatibleCurrenciesException() {
        super("Cannot make operations with money in different currencies");
    }
}
