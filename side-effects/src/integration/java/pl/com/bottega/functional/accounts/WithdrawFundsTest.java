package pl.com.bottega.functional.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.com.bottega.functional.accounts.AccountsController.MoneyDto;
import pl.com.bottega.functional.accounts.AccountsController.WithdrawFundsRequest;

import java.math.BigDecimal;

import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.FIRST_CUSTOMER;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.TEN_EUR;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.USD;
import static pl.com.bottega.functional.accounts.ExampleTestData.EUR;
import static pl.com.bottega.functional.accounts.ExampleTestData.aRandomAccountNumber;
import static pl.com.bottega.functional.accounts.ExampleTestData.anAccount;

@IntegrationTest
class WithdrawFundsTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountsClient accountsClient;

    @Test
    void respondsWithBadRequestWhenRequestIsInvalid() {
        // expect
        accountsClient.withdrawMoney(
            aRandomAccountNumber().getValue(), new WithdrawFundsRequest()
        ).expectStatus().isBadRequest();
        accountsClient.withdrawMoney(
            aRandomAccountNumber().getValue(), new WithdrawFundsRequest(new MoneyDto())
        ).expectStatus().isBadRequest();
        accountsClient.withdrawMoney(
            aRandomAccountNumber().getValue(), new WithdrawFundsRequest(new MoneyDto(BigDecimal.TEN, null))
        ).expectStatus().isBadRequest();
        accountsClient.withdrawMoney(
            aRandomAccountNumber().getValue(), new WithdrawFundsRequest(new MoneyDto(null, USD))
        ).expectStatus().isBadRequest();
        accountsClient.withdrawMoney(
            aRandomAccountNumber().getValue(), new WithdrawFundsRequest(new MoneyDto(BigDecimal.TEN, "XYZ"))
        ).expectStatus().isBadRequest();
    }

    @Test
    void respondsWithNotFoundWhenAccountDoesNotExist() {
        // expect
        accountsClient.withdrawMoney(
            aRandomAccountNumber().getValue(), new WithdrawFundsRequest(ExampleIntegrationTestData.TEN_USD)
        ).expectStatus().isNotFound();
    }

    @Test
    void respondsWithUnprocessableEntityWhenThereIsNotEnoughFunds() {
        // given
        // TODO add blocking here after repository api change
        customerRepository.save(FIRST_CUSTOMER);
        var account = anAccount().withCustomerId(FIRST_CUSTOMER.getId()).withBalance(Money.zero(EUR)).withVersion(null).build();
        // TODO add blocking here after repository api change
        accountRepository.save(account);


        // expect
        accountsClient.withdrawMoney(
            account.getNumber().getValue(), new WithdrawFundsRequest(TEN_EUR)
        ).expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void respondsWithUnprocessableEntityWhenWithdrawalIsMadeInAnotherCurrency() {
        // given
        // TODO add blocking here after repository api change
        customerRepository.save(FIRST_CUSTOMER);
        var account = anAccount().withCustomerId(FIRST_CUSTOMER.getId())
            .withBalance(ExampleTestData.TEN_USD).withVersion(null).build();
        // TODO add blocking here after repository api change
        accountRepository.save(account);


        // expect
        accountsClient.withdrawMoney(
            account.getNumber().getValue(), new WithdrawFundsRequest(TEN_EUR)
        ).expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
