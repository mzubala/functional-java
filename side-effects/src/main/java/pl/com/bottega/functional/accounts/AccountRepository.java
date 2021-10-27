package pl.com.bottega.functional.accounts;

interface AccountRepository {
    Account find(AccountNumber accountNumber) throws NoSuchAccountException;
    void save(Account account);
    AccountNumber nextNumber();
    class NoSuchAccountException extends RuntimeException { }
}
