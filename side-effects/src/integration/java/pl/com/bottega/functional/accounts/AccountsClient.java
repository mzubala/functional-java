package pl.com.bottega.functional.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.util.UriBuilder;
import pl.com.bottega.functional.accounts.AccountsController.DepositFundsRequest;
import pl.com.bottega.functional.accounts.AccountsController.OpenAccountRequest;
import pl.com.bottega.functional.accounts.AccountsController.TransferFundsRequest;
import pl.com.bottega.functional.accounts.AccountsController.WithdrawFundsRequest;
import pl.com.bottega.functional.accounts.AccountsReader.AccountDto;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@Component
class AccountsClient {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext context;

    ResponseSpec openAccount(OpenAccountRequest request) {
        return webTestClient.post()
            .uri(to("/accounts"))
            .bodyValue(request)
            .exchange();
    }

    void openAccountAsserting(OpenAccountRequest request) {
        openAccount(request).expectStatus().is2xxSuccessful();
    }

    ResponseSpec getAccounts(UUID customerId) {
        return webTestClient.get().uri(builder("/accounts").andThen(builder ->
            builder.queryParam("customerId", customerId.toString()).build()
        )).exchange();
    }

    ResponseSpec getAccount(String accountNumber) {
        return webTestClient.get().uri(to("/accounts/{number}", accountNumber)).exchange();
    }

    ResponseSpec depositFunds(String accountNumber, DepositFundsRequest request) {
        return webTestClient.post().uri(to("/accounts/{number}/deposit", accountNumber))
            .bodyValue(request).exchange();
    }

    void depositFundsAsserting(String number, DepositFundsRequest request) {
        depositFunds(number, request).expectStatus().is2xxSuccessful();
    }

    ResponseSpec withdrawMoney(String number, WithdrawFundsRequest request) {
        return webTestClient.post().uri(to("/accounts/{number}/withdrawal", number))
            .bodyValue(request).exchange();
    }

    void withdrawMoneyAsserting(String number, WithdrawFundsRequest request) {
        withdrawMoney(number, request).expectStatus().is2xxSuccessful();
    }

    List<AccountDto> getAccountsAsserting(UUID customerId) {
        return getAccounts(customerId)
            .expectStatus().is2xxSuccessful()
            .expectBodyList(AccountDto.class)
            .returnResult().getResponseBody();
    }

    AccountDto getFirstAccount(UUID customerId) {
        List<AccountDto> accounts = getAccountsAsserting(customerId);
        assertThat(accounts).hasSizeGreaterThan(0);
        return getAccountsAsserting(customerId).get(0);
    }

    AccountDto getAccountAsserting(String number) {
        return getAccount(number)
            .expectStatus().is2xxSuccessful().expectBody(AccountDto.class)
            .returnResult().getResponseBody();
    }

    ResponseSpec transferFunds(TransferFundsRequest request) {
        return webTestClient
            .post()
            .uri(to("/accounts/transfer"))
            .bodyValue(request)
            .exchange();
    }

    void transferFundsAsserting(TransferFundsRequest request) {
        transferFunds(request).expectStatus().is2xxSuccessful();
    }

    private Function<UriBuilder, UriBuilder> builder(String path) {
        return uriBuilder -> uriBuilder
            .host("localhost")
            .port(context.getEnvironment().getProperty("local.server.port"))
            .path(path);
    }

    private Function<UriBuilder, URI> to(String path, Object... variables) {
        return uriBuilder -> builder(path).apply(uriBuilder).build(variables);
    }
}
