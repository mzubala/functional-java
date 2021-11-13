package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.functional.accounts.AccountRepository.NoSuchAccountException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class AccountsReaderAdapter implements AccountsReader {

    private final SpringDataAccountRepository repository;

    @Override
    public List<AccountDto> getAccountsOf(CustomerId customerId) {
        return repository.findByCustomerId(customerId.getValue()).map(this::toDto).toStream().collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccount(AccountNumber accountNumber) throws NoSuchAccountException {
        return repository.findByNumber(accountNumber.getValue())
            .map(this::toDto).block();
    }

    private AccountDto toDto(AccountEntity accountEntity) {
        return new AccountDto(
            accountEntity.getNumber(),
            new MoneyDto(accountEntity.getBalance(), accountEntity.getCurrencyCode())
        );
    }
}
