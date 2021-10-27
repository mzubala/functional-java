package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;

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
