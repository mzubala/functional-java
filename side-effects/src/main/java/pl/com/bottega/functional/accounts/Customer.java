package pl.com.bottega.functional.accounts;

import lombok.Value;

import java.util.UUID;

class Customer {
    private final CustomerId customerId;
    private String firstName;
    private String lastName;
    private final Long version;

    Customer(CustomerId customerId, String firstName, String lastName) {
        this(customerId, firstName, lastName, null);
    }

    Customer(CustomerId customerId, String firstName, String lastName, Long version) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.version = version;
    }

    public CustomerId getId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getVersion() {
        return version;
    }
}

@Value
class CustomerId {
    UUID value;
}
