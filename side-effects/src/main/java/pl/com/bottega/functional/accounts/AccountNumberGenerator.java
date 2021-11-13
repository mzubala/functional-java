package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;

// TODO change API so that we return reactive type (Mono)
interface AccountNumberGenerator {
    AccountNumber generate();
}

@AllArgsConstructor
class DefaultAccountNumberGenerator implements AccountNumberGenerator {

    private final AccountRepository accountRepository;

    @Override
    public AccountNumber generate() {
        return accountRepository.nextNumber();
    }
}
