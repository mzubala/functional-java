package pl.com.bottega.functional.accounts;

import reactor.core.publisher.Mono;

// TODO change API so that we return reactive types (Mono)
interface AccountRepository {
    // TODO also get rid of throw as Mono can carry an error
    Mono<Account> find(AccountNumber accountNumber);
    Mono<Void> save(Account account);
    Mono<AccountNumber> nextNumber();
    class NoSuchAccountException extends RuntimeException { }
}
