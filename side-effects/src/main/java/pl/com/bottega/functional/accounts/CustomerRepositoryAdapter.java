package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final SpringDataCustomerRepository repository;

    @Override
    public Customer find(CustomerId customerId) throws CustomerNotFoundException {
        return repository.findById(customerId.getValue()).orElseThrow(CustomerNotFoundException::new).toDomain();
    }

    @Override
    public void save(Customer customer) {
        repository.save(CustomerEntity.of(customer));
    }
}

interface SpringDataCustomerRepository extends Repository<CustomerEntity, UUID> {
    void save(CustomerEntity customerEntity);

    Optional<CustomerEntity> findById(UUID id);
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("customers")
class CustomerEntity {
    @Id
    private UUID id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Version
    private Long version;

    static CustomerEntity of(Customer customer) {
        return new CustomerEntity(
                customer.getId().getValue(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getVersion()
        );
    }

    public Customer toDomain() {
        return new Customer(new CustomerId(id), firstName, lastName, version);
    }
}