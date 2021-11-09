package pl.com.bottega.functional.accounts;

import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.com.bottega.functional.accounts.ExampleTestData.FIRST_ACCOUNT_NUMBER;
import static pl.com.bottega.functional.accounts.ExampleTestData.FIRST_CUSTOMER_ID;
import static pl.com.bottega.functional.accounts.ExampleTestData.FIVE_USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.TEN_EUR;
import static pl.com.bottega.functional.accounts.ExampleTestData.TEN_USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.anAccount;

public class AccountTest {
    @Test
    public void createsNewAccount() {
        // given
        var account = new Account(FIRST_CUSTOMER_ID, FIRST_ACCOUNT_NUMBER, USD);

        // expect
        assertThat(account.getBalance()).isEqualTo(Money.zero(USD));
        assertThat(account.getCustomerId()).isEqualTo(FIRST_CUSTOMER_ID);
        assertThat(account.getNumber()).isEqualTo(FIRST_ACCOUNT_NUMBER);
        assertThat(account.getVersion()).isNull();
    }

    @Test
    public void createsAccountWithVersion() {
        // given
        var account = new Account(FIRST_CUSTOMER_ID, FIRST_ACCOUNT_NUMBER, TEN_USD, 10L);

        // expect
        assertThat(account.getBalance()).isEqualTo(TEN_USD);
        assertThat(account.getCustomerId()).isEqualTo(FIRST_CUSTOMER_ID);
        assertThat(account.getNumber()).isEqualTo(FIRST_ACCOUNT_NUMBER);
        assertThat(account.getVersion()).isEqualTo(10L);
    }

    @Test
    public void creditsAccount() {
        // given
        var account = anAccount().withBalance(FIVE_USD).build();

        // when
        var creditedAccount = account.credit(TEN_USD)
            .flatMap((afterFirstCredit) -> afterFirstCredit.credit(FIVE_USD));

        // then
        assertThat(account.getBalance()).isEqualTo(FIVE_USD);
        assertThat(creditedAccount.get().getBalance()).isEqualTo(new Money(new BigDecimal("20"), USD));
    }

    @Test
    public void cannotCreditAccountInDifferentCurrency() {
        // given
        var account = anAccount().withBalance(FIVE_USD).build();

        // expect
        VavrAssertions.assertThat(account.credit(TEN_EUR)).failBecauseOf(IncompatibleCurrenciesException.class);
    }

    @Test
    public void debitsAccount() {
        // given
        var account = anAccount().withBalance(TEN_USD).build();

        // when
        var debitedAccount = account.debit(FIVE_USD).flatMap((afterFirstDebit) -> afterFirstDebit.debit(FIVE_USD));

        // then
        assertThat(account.getBalance()).isEqualTo(TEN_USD);
        assertThat(debitedAccount.get().getBalance()).isEqualTo(Money.zero(USD));
    }

    @Test
    public void cannotDebitAccountInDifferentCurrency() {
        // given
        var account = anAccount().withBalance(TEN_EUR).build();

        // expect
        VavrAssertions.assertThat(account.debit(FIVE_USD)).failBecauseOf(IncompatibleCurrenciesException.class);
    }

    @Test
    public void cannotDebitAccountBelowZeroBalance() {
        // given
        var account = anAccount().withBalance(FIVE_USD).build();

        // expect
        VavrAssertions.assertThat(account.debit(TEN_USD)).failBecauseOf(InsufficientFundsException.class);
    }
}
