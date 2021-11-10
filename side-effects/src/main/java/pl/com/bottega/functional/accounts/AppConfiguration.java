package pl.com.bottega.functional.accounts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;
import pl.com.bottega.functional.accounts.DepositFundsHandler.DepositFundsCommand;
import pl.com.bottega.functional.accounts.TransferFundsHandler.TransferCommand;
import pl.com.bottega.functional.accounts.WithdrawFundsHandler.WithdrawFundsCommand;

import static pl.com.bottega.functional.accounts.OpenAccountHandler.OpenAccountCommand;

@Configuration
class AppConfiguration {

    @Bean
    Handler<TransferCommand> transferFundsHandler(
            AccountRepository accountRepository,
            TransactionalOperator transactionOperator
    ) {
        return new TransactionalHandler(
                transactionOperator,
                new DefaultTransferFundsHandler(accountRepository)
        );
    }

    @Bean
    Handler<WithdrawFundsCommand> withdrawFundsCommandHandler(
            AccountRepository accountRepository,
            TransactionalOperator transactionOperator
    ) {
        return new TransactionalHandler<>(
                transactionOperator,
                new DefaultWithdrawFundsHandler(accountRepository)
        );
    }

    @Bean
    Handler<DepositFundsCommand> depositFundsCommandHandler(
            AccountRepository accountRepository,
            TransactionalOperator transactionOperator
    ) {
        return new TransactionalHandler<>(
                transactionOperator,
                new DefaultDepositFundsHandler(accountRepository)
        );
    }

    @Bean
    Handler<OpenAccountCommand> openAccountCommandHandler(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            AccountNumberGenerator accountNumberGenerator,
            TransactionalOperator transactionOperator
    ) {
        return new TransactionalHandler<>(
                transactionOperator,
                new DefaultOpenAccountHandler(customerRepository, accountNumberGenerator, accountRepository)
        );
    }

    @Bean
    AccountNumberGenerator accountNumberGenerator(AccountRepository accountRepository) {
        return new DefaultAccountNumberGenerator(accountRepository);
    }
}
