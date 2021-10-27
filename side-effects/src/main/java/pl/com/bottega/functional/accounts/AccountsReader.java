package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.bottega.functional.accounts.AccountRepository.NoSuchAccountException;

import java.math.BigDecimal;
import java.util.List;

interface AccountsReader {

    List<AccountDto> getAccountsOf(CustomerId customerId);

    AccountDto getAccount(AccountNumber accountNumber) throws NoSuchAccountException;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class AccountDto {
        private String number;
        private MoneyDto balance;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class MoneyDto {
        BigDecimal value;
        String currencyCode;
    }

}
