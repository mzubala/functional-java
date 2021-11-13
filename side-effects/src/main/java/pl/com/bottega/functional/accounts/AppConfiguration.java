package pl.com.bottega.functional.accounts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.functional.accounts.DepositFundsHandler.DepositFundsCommand;
import pl.com.bottega.functional.accounts.TransferFundsHandler.TransferCommand;
import pl.com.bottega.functional.accounts.WithdrawFundsHandler.WithdrawFundsCommand;

import static pl.com.bottega.functional.accounts.OpenAccountHandler.OpenAccountCommand;

@Configuration
class AppConfiguration {

    @Bean
    Handler<TransferCommand> transferFundsHandler(
        AccountRepository accountRepository
    ) {
        return new TransactionalHandler(new DefaultTransferFundsHandler(accountRepository));
    }

    @Bean
    Handler<WithdrawFundsCommand> withdrawFundsCommandHandler(AccountRepository accountRepository) {
        return new TransactionalHandler<>(
            new DefaultWithdrawFundsHandler(accountRepository)
        );
    }

    @Bean
    Handler<DepositFundsCommand> depositFundsCommandHandler(
        AccountRepository accountRepository
    ) {
        return new TransactionalHandler<>(
            new DefaultDepositFundsHandler(accountRepository)
        );
    }

    @Bean
    Handler<OpenAccountCommand> openAccountCommandHandler(
        CustomerRepository customerRepository,
        AccountRepository accountRepository,
        AccountNumberGenerator accountNumberGenerator
    ) {
        return new TransactionalHandler<>(
            new DefaultOpenAccountHandler(customerRepository, accountNumberGenerator, accountRepository)
        );
    }

    @Bean
    AccountNumberGenerator accountNumberGenerator(AccountRepository accountRepository) {
        return new DefaultAccountNumberGenerator(accountRepository);
    }
}
