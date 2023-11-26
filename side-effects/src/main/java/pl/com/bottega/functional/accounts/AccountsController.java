package pl.com.bottega.functional.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.functional.accounts.AccountRepository.NoSuchAccountException;
import pl.com.bottega.functional.accounts.AccountsReader.AccountDto;
import pl.com.bottega.functional.accounts.CustomerRepository.CustomerNotFoundException;
import pl.com.bottega.functional.accounts.DepositFundsHandler.DepositFundsCommand;
import pl.com.bottega.functional.accounts.OpenAccountHandler.OpenAccountCommand;
import pl.com.bottega.functional.accounts.TransferFundsHandler.TransferCommand;
import pl.com.bottega.functional.accounts.WithdrawFundsHandler.WithdrawFundsCommand;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
// TODO change to return reactive types
class AccountsController {

    private final CommandGateway commandGateway;
    private final AccountsReader accountsReader;

    @PostMapping
    public void open(@Valid @RequestBody OpenAccountRequest request) {
        commandGateway.execute(request.toCommand());
    }

    @PostMapping("/{accountNumber}/deposit")
    public void deposit(@PathVariable String accountNumber, @Valid @RequestBody DepositFundsRequest request) {
        commandGateway.execute(request.toCommand(accountNumber));
    }

    @PostMapping("/{accountNumber}/withdrawal")
    public void withdraw(@PathVariable String accountNumber, @Valid @RequestBody WithdrawFundsRequest withdrawFundsRequest) {
        commandGateway.execute(withdrawFundsRequest.toCommand(accountNumber));
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferFundsRequest request) {
        commandGateway.execute(request.toCommand());
    }

    @GetMapping("/{number}")
    public AccountDto getAccount(@PathVariable String number) {
        return accountsReader.getAccount(new AccountNumber(number));
    }

    @GetMapping
    public List<AccountDto> getAccountsOf(@RequestParam String customerId) {
        return accountsReader.getAccountsOf(new CustomerId(UUID.fromString(customerId)));
    }

    @ExceptionHandler(InvalidCurrencyCode.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleInvalidCurrencyCode() {
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleCustomerNotFoundException() {
    }

    @ExceptionHandler(NoSuchAccountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoSuchAccountException() {
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleInsufficientFundsException() {
    }

    @ExceptionHandler(IncompatibleCurrenciesException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleIncompatibleCurrenciesException() {}

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class OpenAccountRequest {
        @NotNull
        UUID customerId;

        @NotBlank
        String currencyCode;

        public OpenAccountCommand toCommand() {
            return new OpenAccountCommand(
                new CustomerId(customerId),
                MoneyDto.toCurrency(currencyCode)
            );
        }
    }

    static class InvalidCurrencyCode extends RuntimeException {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static
    class DepositFundsRequest {
        @NonNull
        @Valid
        private MoneyDto amount;

        public DepositFundsCommand toCommand(String accountNumber) {
            return new DepositFundsCommand(
                new AccountNumber(accountNumber),
                amount.toMoney()
            );
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class WithdrawFundsRequest {
        @NonNull
        @Valid
        private MoneyDto amount;

        public WithdrawFundsCommand toCommand(String accountNumber) {
            return new WithdrawFundsCommand(
                new AccountNumber(accountNumber),
                amount.toMoney()
            );
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class TransferFundsRequest {
        @NotBlank
        private String sourceAccountNumber;

        @NotBlank
        private String destinationAccountNumber;

        @NonNull
        @Valid
        private MoneyDto amount;

        public TransferCommand toCommand() {
            return new TransferCommand(
                new AccountNumber(sourceAccountNumber),
                new AccountNumber(destinationAccountNumber),
                amount.toMoney()
            );
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class MoneyDto {
        @NotNull
        @Positive
        BigDecimal value;

        @Size(min = 3, max = 3)
        @NotNull
        String currencyCode;

        public Money toMoney() {
            return new Money(value, toCurrency(currencyCode));
        }

        static Currency toCurrency(String currencyCode) {
            try {
                return Currency.getInstance(currencyCode);
            } catch (IllegalArgumentException ex) {
                throw new InvalidCurrencyCode();
            }
        }
    }
}

