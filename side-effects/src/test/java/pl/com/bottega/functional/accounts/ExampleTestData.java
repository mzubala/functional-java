package pl.com.bottega.functional.accounts;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

class ExampleTestData {

    private static final Faker FAKER = new Faker();

    static final CustomerId FIRST_CUSTOMER_ID = aRandomCustomerId();
    static final CustomerId SECOND_CUSTOMER_ID = aRandomCustomerId();
    static final Customer FIRST_CUSTOMER = new Customer(FIRST_CUSTOMER_ID, "John", "Doe");
    static final Customer SECOND_CUSTOMER = new Customer(SECOND_CUSTOMER_ID, "Jane", "Doe");
    static final Currency USD = Currency.getInstance("USD");
    static final Currency EUR = Currency.getInstance("EUR");
    static final Money TEN_USD = new Money(BigDecimal.TEN, USD);
    static final Money TEN_EUR = new Money(BigDecimal.TEN, EUR);
    static final Money FIVE_USD = new Money(new BigDecimal("5"), USD);
    static final AccountNumber FIRST_ACCOUNT_NUMBER = aRandomAccountNumber();


    static CustomerId aRandomCustomerId() {
        return new CustomerId(UUID.randomUUID());
    }

    static AccountNumber aRandomAccountNumber() {
        return new AccountNumber(FAKER.numerify("#################"));
    }

    static AccountExample anAccount() {
        return new AccountExample();
    }

    @With
    @AllArgsConstructor
    @NoArgsConstructor
    static class AccountExample {
        CustomerId customerId = aRandomCustomerId();
        AccountNumber number = aRandomAccountNumber();
        Money balance = TEN_USD;
        Long version = 10L;

        Account build() {
            return new Account(customerId, number, balance, version);
        }
    }
}
