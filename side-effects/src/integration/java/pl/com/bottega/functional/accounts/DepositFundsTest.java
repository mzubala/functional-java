package pl.com.bottega.functional.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.functional.accounts.AccountsController.DepositFundsRequest;
import pl.com.bottega.functional.accounts.AccountsController.MoneyDto;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.FIRST_CUSTOMER;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.TEN_USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.EUR;
import static pl.com.bottega.functional.accounts.ExampleTestData.aRandomAccountNumber;
import static pl.com.bottega.functional.accounts.ExampleTestData.anAccount;

@IntegrationTest
public class DepositFundsTest {

    @Autowired
    private AccountsClient client;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void respondsWithBadRequestInCaseOfInvalidData() {
        // expect
        client.depositFunds(aRandomAccountNumber().getValue(), new DepositFundsRequest()).expectStatus().isBadRequest();
        client.depositFunds(
            aRandomAccountNumber().getValue(),
            new DepositFundsRequest(new MoneyDto(null, "USD"))
        ).expectStatus().isBadRequest();
        client.depositFunds(
            aRandomAccountNumber().getValue(),
            new DepositFundsRequest(new MoneyDto(BigDecimal.TEN, null))
        ).expectStatus().isBadRequest();
        client.depositFunds(
            aRandomAccountNumber().getValue(),
            new DepositFundsRequest(new MoneyDto(BigDecimal.TEN, "XYZ"))
        ).expectStatus().isBadRequest();
    }

    @Test
    void respondsWithNotFoundWhenAccountDoesNotExist() {
        // expect
        client.depositFunds(aRandomAccountNumber().getValue(), new DepositFundsRequest(TEN_USD)).expectStatus().isNotFound();
    }

    @Test
    void respondsWithUnprocessableEntityWhenAccountCurrencyIsDifferentThanDepositAmount() {
        // given
        customerRepository.save(FIRST_CUSTOMER).block();
        var account = anAccount().withCustomerId(FIRST_CUSTOMER.getId()).withBalance(Money.zero(EUR)).withVersion(null).build();
        accountRepository.save(account).block();

        // expect
        client.depositFunds(
            account.getNumber().getValue(), new DepositFundsRequest(TEN_USD)
        ).expectStatus().isEqualTo(UNPROCESSABLE_ENTITY);
    }
}
