package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Component
@AllArgsConstructor
class AccountRepositoryAdapter implements AccountRepository {

    private final SpringDataAccountRepository repository;

    @Override
    public Mono<Account> find(AccountNumber accountNumber) throws NoSuchAccountException {
        return repository.findByNumber(accountNumber.getValue())
            .switchIfEmpty(Mono.error(NoSuchAccountException::new))
            .map(AccountEntity::toDomain);
    }

    @Override
    public Mono<Void> save(Account account) {
        return repository.save(AccountEntity.of(account)).then();
    }

    @Override
    public Mono<AccountNumber> nextNumber() {
        return repository.nextAccountSequenceNumber().map((number) -> {
            var numberString = String.format("%016d", number);
            return new AccountNumber(numberString);
        });
    }
}

interface SpringDataAccountRepository extends Repository<AccountEntity, String> {
    Mono<AccountEntity> findByNumber(String number);

    Mono<AccountEntity> save(AccountEntity account);

    @Query("SELECT nextval('account_number_sequence')")
    Mono<Long> nextAccountSequenceNumber();

    Flux<AccountEntity> findByCustomerId(UUID customerId);
}

@Table("accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
class AccountEntity {
    @Id
    private String number;

    @Column("customer_id")
    private UUID customerId;

    private BigDecimal balance;

    @Column("currency_code")
    private String currencyCode;

    @Version
    private Long version;

    static AccountEntity of(Account account) {
        var balance = account.getBalance();
        return new AccountEntity(
            account.getNumber().getValue(),
            account.getCustomerId().getValue(),
            balance.getValue(),
            balance.getCurrency().getCurrencyCode(),
            account.getVersion()
        );
    }

    Account toDomain() {
        return new Account(
            new CustomerId(customerId),
            new AccountNumber(number),
            new Money(balance, Currency.getInstance(currencyCode)),
            version
        );
    }
}
