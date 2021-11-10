package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.functional.accounts.AccountRepository.NoSuchAccountException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class AccountsReaderAdapter implements AccountsReader {

    private final SpringDataAccountRepository repository;

    @Override
    public Flux<AccountDto> getAccountsOf(CustomerId customerId) {
        return repository.findByCustomerId(customerId.getValue()).map(this::toDto);
    }

    @Override
    public Mono<AccountDto> getAccount(AccountNumber accountNumber) {
        return repository.findByNumber(accountNumber.getValue())
            .map(this::toDto)
            .switchIfEmpty(Mono.error(NoSuchAccountException::new));
    }

    private AccountDto toDto(AccountEntity accountEntity) {
        return new AccountDto(
            accountEntity.getNumber(),
            new MoneyDto(accountEntity.getBalance(), accountEntity.getCurrencyCode())
        );
    }
}
