package pl.com.bottega.functional.accounts;

public interface CustomerRepository {
    Customer find(CustomerId customerId) throws CustomerNotFoundException;
    void save(Customer customer);

    class CustomerNotFoundException extends RuntimeException {}
}
