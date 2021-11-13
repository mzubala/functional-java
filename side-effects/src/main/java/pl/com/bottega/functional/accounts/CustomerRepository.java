package pl.com.bottega.functional.accounts;

import reactor.core.publisher.Mono;

public interface CustomerRepository {
    Mono<Customer> find(CustomerId customerId);
    Mono<Void> save(Customer customer);

    class CustomerNotFoundException extends RuntimeException {}
}
