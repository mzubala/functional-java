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

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
class AccountRepositoryAdapter implements AccountRepository {

    private final SpringDataAccountRepository repository;

    @Override
    public Account find(AccountNumber accountNumber) throws NoSuchAccountException {
        return repository.findByNumber(accountNumber.getValue())
                .orElseThrow(NoSuchAccountException::new)
                .toDomain();
    }

    @Override
    public void save(Account account) {
        repository.save(AccountEntity.of(account));
    }

    @Override
    public AccountNumber nextNumber() {
        var number = repository.nextAccountSequenceNumber();
        var numberString = String.format("%016d", number);
        return new AccountNumber(numberString);
    }
}

interface SpringDataAccountRepository extends Repository<AccountEntity, String> {
    Optional<AccountEntity> findByNumber(String number);

    void save(AccountEntity account);

    @Query("SELECT nextval('account_number_sequence')")
    Long nextAccountSequenceNumber();

    Stream<AccountEntity> findByCustomerId(UUID customerId);
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
