package pl.com.bottega.functional.accounts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;
import pl.com.bottega.functional.accounts.DepositFundsHandler.DepositFundsCommand;
import pl.com.bottega.functional.accounts.TransferFundsHandler.TransferCommand;
import pl.com.bottega.functional.accounts.WithdrawFundsHandler.WithdrawFundsCommand;

import static pl.com.bottega.functional.accounts.OpenAccountHandler.*;

@Configuration
class AppConfiguration {

    @Bean
    Handler<TransferCommand> transferFundsHandler(
            AccountRepository accountRepository,
            TransactionTemplate transactionTemplate
    ) {
        return new TransactionalHandler(
                transactionTemplate,
                new DefaultTransferFundsHandler(accountRepository)
        );
    }

    @Bean
    Handler<WithdrawFundsCommand> withdrawFundsCommandHandler(
            AccountRepository accountRepository,
            TransactionTemplate transactionTemplate
    ) {
        return new TransactionalHandler<>(
                transactionTemplate,
                new DefaultWithdrawFundsHandler(accountRepository)
        );
    }

    @Bean
    Handler<DepositFundsCommand> depositFundsCommandHandler(
            AccountRepository accountRepository,
            TransactionTemplate transactionTemplate
    ) {
        return new TransactionalHandler<>(
                transactionTemplate,
                new DefaultDepositFundsHandler(accountRepository)
        );
    }

    @Bean
    Handler<OpenAccountCommand> openAccountCommandHandler(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            AccountNumberGenerator accountNumberGenerator,
            TransactionTemplate transactionTemplate
    ) {
        return new TransactionalHandler<>(
                transactionTemplate,
                new DefaultOpenAccountHandler(customerRepository, accountNumberGenerator, accountRepository)
        );
    }

    @Bean
    AccountNumberGenerator accountNumberGenerator(AccountRepository accountRepository) {
        return new DefaultAccountNumberGenerator(accountRepository);
    }
}
