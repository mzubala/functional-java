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
        return checkCurrencies(other).map(this::safeAdd);
    }

    private Try<Money> checkCurrencies(Money other) {
        if(other.currency.equals(currency)) {
            return Try.success(other);
        } else {
            return Try.failure(new IncompatibleCurrenciesException());
        }
    }

    private Money safeAdd(Money other) {
        return new Money(value.add(other.value), currency);
    }

    Try<Money> subtract(Money other) {
        if(other.currency.equals(currency)) {
            return Try.success(new Money(value.subtract(other.value), currency));
        } else {
            return Try.failure(new IncompatibleCurrenciesException());
        }
    }

    Try<Boolean> isGreaterThan(Money other) {
        if(other.currency.equals(currency)) {
            return Try.success(value.compareTo(other.value) > 0);
        } else {
            return Try.failure(new IncompatibleCurrenciesException());
        }
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
