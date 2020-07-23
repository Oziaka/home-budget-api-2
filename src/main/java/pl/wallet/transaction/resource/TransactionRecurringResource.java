package pl.wallet.transaction.resource;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wallet.transaction.controller.TransactionRecurringController;

@Validated
@RestController
@RequestMapping("/wallet/{walletId}/transaction_recurring")
@AllArgsConstructor
@CrossOrigin("${cors.allowed-origins}")
public class TransactionRecurringResource {

    private TransactionRecurringController transactionRecurringController;

}
