package pl.com.bottega.functional.accounts;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.Supplier;

import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static pl.com.bottega.functional.accounts.ExampleTestData.FIVE_USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.TEN_EUR;
import static pl.com.bottega.functional.accounts.ExampleTestData.TEN_USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.USD;

class MoneyTest {

    @Test
    void addsMoney() {
        // expect
        assertThat(TEN_USD.add(FIVE_USD)).contains(new Money(new BigDecimal("15"), USD));
    }

    @Test
    void subtractsMoney() {
        // expect
        assertThat(TEN_USD.subtract(FIVE_USD)).contains(FIVE_USD);
    }

    @Test
    void comparesMoney() {
        // expect
        assertThat(TEN_USD.isGreaterThan(FIVE_USD)).contains(true);
        assertThat(TEN_USD.isGreaterThan(FIVE_USD)).contains(true);
        assertThat(TEN_USD.isGreaterThanOrEqualTo(FIVE_USD)).contains(true);
        assertThat(TEN_USD.isGreaterThanOrEqualTo(FIVE_USD)).contains(true);
        assertThat(TEN_USD.isGreaterThanOrEqualTo(TEN_USD)).contains(true);
        assertThat(TEN_USD.isGreaterThanOrEqualTo(TEN_USD)).contains(true);
        assertThat(FIVE_USD.isLessThan(TEN_USD)).contains(true);
        assertThat(FIVE_USD.isLessThan(TEN_USD)).contains(true);
        assertThat(FIVE_USD.isLessThanOrEqualTo(TEN_USD)).contains(true);
        assertThat(FIVE_USD.isLessThanOrEqualTo(TEN_USD)).contains(true);
        assertThat(FIVE_USD.isLessThanOrEqualTo(FIVE_USD)).contains(true);
        assertThat(FIVE_USD.isLessThanOrEqualTo(FIVE_USD)).contains(true);
    }

    @Test
    void cannotPerformOperationsOnMoneyInDifferentCurrencies() {
        // expect
        assertIncompatibleCurrenciesException(() -> TEN_USD.add(TEN_EUR));
        assertIncompatibleCurrenciesException(() -> TEN_USD.subtract(TEN_EUR));
        assertIncompatibleCurrenciesException(() -> TEN_USD.isLessThan(TEN_EUR));
        assertIncompatibleCurrenciesException(() -> TEN_USD.isLessThanOrEqualTo(TEN_EUR));
        assertIncompatibleCurrenciesException(() -> TEN_USD.isGreaterThan(TEN_EUR));
        assertIncompatibleCurrenciesException(() -> TEN_USD.isGreaterThanOrEqualTo(TEN_EUR));
    }

    private void assertIncompatibleCurrenciesException(Supplier<Try<?>> supplier) {
        assertThat(supplier.get()).isFailure().failBecauseOf(IncompatibleCurrenciesException.class);
    }
}
