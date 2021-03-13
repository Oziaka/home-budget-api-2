package pl.wallet.transaction.resource;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.wallet.transaction.dto.TransactionRecurringDto;
import pl.wallet.transaction.service.TransactionRecurringService;

import javax.validation.Valid;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("/wallet/{walletId}/transaction_recurring")
@AllArgsConstructor
@CrossOrigin("${cors.allowed-origins}")
public class TransactionRecurringResource {

   private TransactionRecurringService transactionRecurringService;

   @PutMapping("/add")
   public ResponseEntity<TransactionRecurringDto> addTransactionRecurring(Principal principal, @PathVariable Long walletId, @Valid @RequestBody TransactionRecurringDto transactionRecurringDto) {
      return ResponseEntity.ok(transactionRecurringService.addTransactionRecurring(principal, walletId, transactionRecurringDto));
   }


   @PostMapping("/{transactionRecurringId}/remove")
   public ResponseEntity<TransactionRecurringDto> removeTransactionRecurring(Principal principal, @PathVariable Long walletId, @PathVariable Long transactionRecurringId) {
      transactionRecurringService.removeTransactionRecurring(principal, walletId, transactionRecurringId);
      return ResponseEntity.noContent().build();
   }
}
