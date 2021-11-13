package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.functional.accounts.AccountRepository.NoSuchAccountException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class AccountsReaderAdapter implements AccountsReader {

    private final SpringDataAccountRepository repository;

    @Override
    public List<AccountDto> getAccountsOf(CustomerId customerId) {
        // TODO convert Flux to Stream then collect to List
        return repository.findByCustomerId(customerId.getValue()).map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccount(AccountNumber accountNumber) throws NoSuchAccountException {
        // TODO just block
        return repository.findByNumber(accountNumber.getValue())
            .map(this::toDto)
            .orElseThrow(NoSuchAccountException::new);
    }

    private AccountDto toDto(AccountEntity accountEntity) {
        return new AccountDto(
            accountEntity.getNumber(),
            new MoneyDto(accountEntity.getBalance(), accountEntity.getCurrencyCode())
        );
    }
}
