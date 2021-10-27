package pl.com.bottega.functional.accounts;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Value
class Money implements Comparable<Money> {

    @NonNull
    BigDecimal value;

    @NonNull
    Currency currency;

    static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    Money add(Money other) {
        ensureSameCurrencies(other);
        return new Money(value.add(other.value), currency);
    }

    Money subtract(Money other) {
        ensureSameCurrencies(other);
        return new Money(value.subtract(other.value), currency);
    }

    boolean isGreaterThan(Money other) {
        return compareTo(other) > 0;
    }

    boolean isLessThan(Money other) {
        return compareTo(other) < 0;
    }

    boolean isGreaterThanOrEqualTo(Money other) {
        return compareTo(other) >= 0;
    }

    boolean isLessThanOrEqualTo(Money other) {
        return compareTo(other) <= 0;
    }

    @Override
    public int compareTo(Money other) {
        ensureSameCurrencies(other);
        return value.compareTo(other.value);
    }

    private void ensureSameCurrencies(Money other) {
        if(!other.currency.equals(currency)) {
            throw new IncompatibleCurrenciesException();
        }
    }
}

class IncompatibleCurrenciesException extends RuntimeException {
    IncompatibleCurrenciesException() {
        super("Cannot make operations with money in different currencies");
    }
}
