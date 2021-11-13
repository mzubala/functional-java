package pl.com.bottega.functional.accounts;

// TODO change API so that we return reactive types (Mono)
interface AccountRepository {
    // TODO also get rid of throw as Mono can carry an error
    Account find(AccountNumber accountNumber) throws NoSuchAccountException;
    void save(Account account);
    AccountNumber nextNumber();
    class NoSuchAccountException extends RuntimeException { }
}
