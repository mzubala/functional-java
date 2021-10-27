package pl.com.bottega.functional.accounts;

import pl.com.bottega.functional.accounts.AccountsController.MoneyDto;

import java.math.BigDecimal;
import java.util.UUID;

class ExampleIntegrationTestData {
    static final UUID FIRST_CUSTOMER_ID = aRandomCustomerId();
    static final UUID SECOND_CUSTOMER_ID = aRandomCustomerId();
    static final Customer FIRST_CUSTOMER = new Customer(new CustomerId(FIRST_CUSTOMER_ID), "John", "Doe");
    static final Customer SECOND_CUSTOMER = new Customer(new CustomerId(SECOND_CUSTOMER_ID), "Jane", "Doe");
    static final String USD = "USD";
    static final String EUR = "EUR";
    static final MoneyDto TEN_USD = new MoneyDto(BigDecimal.TEN, USD);
    static final MoneyDto TEN_EUR = new MoneyDto(BigDecimal.TEN, EUR);
    static final MoneyDto FIVE_USD = new MoneyDto(new BigDecimal("5"), USD);

    static UUID aRandomCustomerId() {
        return UUID.randomUUID();
    }
}
