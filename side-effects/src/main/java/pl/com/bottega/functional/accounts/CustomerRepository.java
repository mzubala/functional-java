package pl.com.bottega.functional.accounts;

// TODO return reactive type (Mono)
public interface CustomerRepository {
    // TODO no throw bc Mono can carry an error
    Customer find(CustomerId customerId) throws CustomerNotFoundException;
    void save(Customer customer);

    class CustomerNotFoundException extends RuntimeException {}
}
