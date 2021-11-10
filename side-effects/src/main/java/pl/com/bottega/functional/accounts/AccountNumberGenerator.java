package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

interface AccountNumberGenerator {
    Mono<AccountNumber> generate();
}

@AllArgsConstructor
class DefaultAccountNumberGenerator implements AccountNumberGenerator {

    private final AccountRepository accountRepository;

    @Override
    public Mono<AccountNumber> generate() {
        return accountRepository.nextNumber();
    }
}
