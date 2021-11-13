package pl.com.bottega.functional.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.functional.accounts.AccountsController.DepositFundsRequest;
import pl.com.bottega.functional.accounts.AccountsController.OpenAccountRequest;
import pl.com.bottega.functional.accounts.AccountsController.TransferFundsRequest;
import pl.com.bottega.functional.accounts.AccountsController.WithdrawFundsRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.EUR;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.FIRST_CUSTOMER;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.FIRST_CUSTOMER_ID;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.FIVE_USD;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.SECOND_CUSTOMER;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.SECOND_CUSTOMER_ID;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.TEN_EUR;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.TEN_USD;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.USD;

@IntegrationTest
class E2ETests {

    @Autowired
    private AccountsClient accountsClient;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        // TODO add blocking here after repository api change
        customerRepository.save(FIRST_CUSTOMER);
        // TODO add blocking here after repository api change
        customerRepository.save(SECOND_CUSTOMER);
    }

    @Test
    public void opensAccountAndDepositsFunds() {
        accountsClient.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, USD));
        var openedAccount = accountsClient.getFirstAccount(FIRST_CUSTOMER_ID);
        accountsClient.depositFundsAsserting(openedAccount.getNumber(), new DepositFundsRequest(TEN_USD));
        accountsClient.depositFundsAsserting(openedAccount.getNumber(), new DepositFundsRequest(FIVE_USD));
        var accountAfterDeposit = accountsClient.getAccountAsserting(openedAccount.getNumber());
        assertThat(accountAfterDeposit.getBalance()).isEqualTo(new AccountsReader.MoneyDto(new BigDecimal("15"), USD));
    }

    @Test
    public void withdrawsFunds() {
        accountsClient.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, USD));
        var openedAccount = accountsClient.getFirstAccount(FIRST_CUSTOMER_ID);
        accountsClient.depositFundsAsserting(openedAccount.getNumber(), new DepositFundsRequest(TEN_USD));
        accountsClient.withdrawMoneyAsserting(openedAccount.getNumber(), new WithdrawFundsRequest(FIVE_USD));
        var accountAfterDeposit = accountsClient.getAccountAsserting(openedAccount.getNumber());
        assertThat(accountAfterDeposit.getBalance()).isEqualTo(new AccountsReader.MoneyDto(new BigDecimal("5"), USD));
    }

    @Test
    public void transfersFunds() {
        accountsClient.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, USD));
        accountsClient.openAccount(new OpenAccountRequest(SECOND_CUSTOMER_ID, USD));
        var firstCustomerAccount = accountsClient.getFirstAccount(FIRST_CUSTOMER_ID);
        var secondCustomerAccount = accountsClient.getFirstAccount(SECOND_CUSTOMER_ID);

        accountsClient.depositFundsAsserting(firstCustomerAccount.getNumber(), new DepositFundsRequest(TEN_USD));
        accountsClient.depositFundsAsserting(secondCustomerAccount.getNumber(), new DepositFundsRequest(FIVE_USD));
        accountsClient.transferFundsAsserting(
            new TransferFundsRequest(
                firstCustomerAccount.getNumber(),
                secondCustomerAccount.getNumber(),
                TEN_USD
            )
        );

        firstCustomerAccount = accountsClient.getFirstAccount(FIRST_CUSTOMER_ID);
        secondCustomerAccount = accountsClient.getFirstAccount(SECOND_CUSTOMER_ID);
        assertThat(firstCustomerAccount.getBalance()).isEqualTo(new AccountsReader.MoneyDto(BigDecimal.ZERO, USD));
        assertThat(secondCustomerAccount.getBalance()).isEqualTo(new AccountsReader.MoneyDto(new BigDecimal("15"), USD));
    }

    @Test
    public void customerCanHaveMultipleAccounts() {
        accountsClient.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, USD));
        accountsClient.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, EUR));

        var accounts = accountsClient.getAccountsAsserting(FIRST_CUSTOMER_ID);
        var firstAccount = accounts.get(0);
        var secondAccount = accounts.get(1);
        accountsClient.depositFunds(firstAccount.getNumber(), new DepositFundsRequest(FIVE_USD));
        accountsClient.depositFunds(secondAccount.getNumber(), new DepositFundsRequest(TEN_EUR));

        accounts = accountsClient.getAccountsAsserting(FIRST_CUSTOMER_ID);
        firstAccount = accounts.get(0);
        secondAccount = accounts.get(1);
        assertThat(firstAccount.getBalance()).isEqualTo(new AccountsReader.MoneyDto(new BigDecimal("5"), USD));
        assertThat(secondAccount.getBalance()).isEqualTo(new AccountsReader.MoneyDto(new BigDecimal("10"), EUR));
    }
}
