package pl.com.bottega.functional.accounts;

import reactor.core.publisher.Mono;

interface AccountRepository {
    Mono<Account> find(AccountNumber accountNumber);
    Mono<Void> save(Account account);
    Mono<AccountNumber> nextNumber();
    class NoSuchAccountException extends RuntimeException { }
}
