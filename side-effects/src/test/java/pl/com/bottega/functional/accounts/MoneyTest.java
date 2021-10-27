package pl.com.bottega.functional.accounts;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static pl.com.bottega.functional.accounts.ExampleTestData.FIVE_USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.TEN_EUR;
import static pl.com.bottega.functional.accounts.ExampleTestData.TEN_USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.USD;

class MoneyTest {

    @Test
    void addsMoney() {
        // expect
        assertThat(TEN_USD.add(FIVE_USD)).isEqualTo(new Money(new BigDecimal("15"), USD));
    }

    @Test
    void subtractsMoney() {
        // expect
        assertThat(TEN_USD.subtract(FIVE_USD)).isEqualTo(FIVE_USD);
    }

    @Test
    void comparesMoney() {
        // expect
        assertThat(TEN_USD).isGreaterThan(FIVE_USD);
        assertThat(TEN_USD.isGreaterThan(FIVE_USD)).isTrue();
        assertThat(TEN_USD).isGreaterThanOrEqualTo(FIVE_USD);
        assertThat(TEN_USD.isGreaterThanOrEqualTo(FIVE_USD)).isTrue();
        assertThat(TEN_USD).isGreaterThanOrEqualTo(TEN_USD);
        assertThat(TEN_USD.isGreaterThanOrEqualTo(TEN_USD)).isTrue();
        assertThat(FIVE_USD).isLessThan(TEN_USD);
        assertThat(FIVE_USD.isLessThan(TEN_USD)).isTrue();
        assertThat(FIVE_USD).isLessThanOrEqualTo(TEN_USD);
        assertThat(FIVE_USD.isLessThanOrEqualTo(TEN_USD)).isTrue();
        assertThat(FIVE_USD).isLessThanOrEqualTo(FIVE_USD);
        assertThat(FIVE_USD.isLessThanOrEqualTo(FIVE_USD)).isTrue();
    }

    @Test
    void cannotPerformOperationsOnMoneyInDifferentCurrencies() {
        // expect
        assertIncompatibleCurrencieException(() -> TEN_USD.add(TEN_EUR));
        assertIncompatibleCurrencieException(() -> TEN_USD.subtract(TEN_EUR));
        assertIncompatibleCurrencieException(() -> TEN_USD.compareTo(TEN_EUR));
        assertIncompatibleCurrencieException(() -> TEN_USD.isLessThan(TEN_EUR));
        assertIncompatibleCurrencieException(() -> TEN_USD.isLessThanOrEqualTo(TEN_EUR));
        assertIncompatibleCurrencieException(() -> TEN_USD.isGreaterThan(TEN_EUR));
        assertIncompatibleCurrencieException(() -> TEN_USD.isGreaterThanOrEqualTo(TEN_EUR));
    }

    private void assertIncompatibleCurrencieException(Runnable runnable) {
        assertThatThrownBy(runnable::run).isInstanceOf(IncompatibleCurrenciesException.class);
    }
}
