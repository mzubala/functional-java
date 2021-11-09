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
        return ensureSameCurrencies(other).map(this::safeAdd);
    }

    private Money safeAdd(Money other) {
        return new Money(value.add(other.value), currency);
    }

    Try<Money> subtract(Money other) {
        return ensureSameCurrencies(other).map(this::safeSubtract);
    }

    private Money safeSubtract(Money other) {
        return new Money(value.subtract(other.value), currency);
    }

    Try<Boolean> isGreaterThan(Money other) {
        return compareTo(other).map((result) -> result > 0);
    }

    Try<Boolean> isLessThan(Money other) {
        return compareTo(other).map((result) -> result < 0);
    }

    Try<Boolean> isGreaterThanOrEqualTo(Money other) {
        return compareTo(other).map((result) -> result >= 0);
    }

    Try<Boolean> isLessThanOrEqualTo(Money other) {
        return compareTo(other).map((result) -> result <= 0);
    }

    private Try<Integer> compareTo(Money other) {
        return ensureSameCurrencies(other).map(this::safeCompareTo);
    }

    private int safeCompareTo(Money other) {
        return value.compareTo(other.value);
    }

    private Try<Money> ensureSameCurrencies(Money other) {
        if (!other.currency.equals(currency)) {
            return Try.failure(new IncompatibleCurrenciesException());
        }
        return Try.success(other);
    }
}

class IncompatibleCurrenciesException extends RuntimeException {
    IncompatibleCurrenciesException() {
        super("Cannot make operations with money in different currencies");
    }
}
