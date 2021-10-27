package pl.com.bottega.functional.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.functional.accounts.AccountsController.OpenAccountRequest;

import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.FIRST_CUSTOMER_ID;
import static pl.com.bottega.functional.accounts.ExampleIntegrationTestData.USD;

@IntegrationTest
class OpenAccountTest {

    @Autowired
    private AccountsClient client;

    @Test
    public void respondsWith400WhenRequestIsInvalid() {
        // expect
        client.openAccount(new OpenAccountRequest(null, USD)).expectStatus().isBadRequest();
        client.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, null)).expectStatus().isBadRequest();
        client.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, "wrong")).expectStatus().isBadRequest();
        client.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, "XYZ")).expectStatus().isBadRequest();
    }

    @Test
    public void respondsWith404WhenCustomerDoesNotExist() {
        client.openAccount(new OpenAccountRequest(FIRST_CUSTOMER_ID, "EUR")).expectStatus().isNotFound();
    }
}
