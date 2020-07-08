package pl.wallet.transaction;

import lombok.AllArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.NotEqual;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/wallet/{walletId}/transaction")
@AllArgsConstructor
@CrossOrigin("${cors.allowed-origins}")
public class TransactionResource {
  private TransactionController transactionController;

  @PutMapping(value = "/add")
  public ResponseEntity<TransactionDto> addTransaction (Principal principal, @PathVariable Long walletId, @Valid @RequestBody TransactionDto transactionDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(transactionController.addTransaction(principal, walletId, transactionDto));
  }

  @GetMapping(value = "/{transactionId}", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<TransactionDto> getTransaction (Principal principal, @PathVariable Long walletId, @PathVariable Long transactionId) {
    return ResponseEntity.ok(transactionController.getTransaction(principal, walletId, transactionId));
  }

  @PostMapping(value = "/{transactionId}/edit", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<TransactionDto> editTransaction (Principal principal, @PathVariable Long walletId, @PathVariable Long transactionId, @Valid @RequestBody TransactionDto transactionDto) {
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(transactionController.editTransaction(principal, walletId, transactionId, transactionDto));
  }

  @DeleteMapping(value = "/{transactionId}/remove", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
  public ResponseEntity removeTransaction (Principal principal, @PathVariable Long walletId, @PathVariable Long transactionId) {
    transactionController.removeTransaction(principal, walletId, transactionId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<List<TransactionDto>> getWalletTransactions (Principal principal,
                                                                     @PageableDefault(page = 0, size = 40)
                                                                     @SortDefault.SortDefaults({
                                                                       @SortDefault(sort = "dateOfPurchase", direction = Sort.Direction.DESC),
                                                                       @SortDefault(sort = "name", direction = Sort.Direction.ASC)})
                                                                       Pageable pageable,
                                                                     @And({
                                                                       @Spec(path = "category.transactionType", params = "transactionType", spec = Equal.class),
                                                                       @Spec(path = "isFinished", params = "isFinished", spec = Equal.class),
                                                                       @Spec(path = "wallet.id", pathVars = "walletId", spec = Equal.class),
                                                                       @Spec(path = "price", params = "minPrice", spec = GreaterThanOrEqual.class),
                                                                       @Spec(path = "price", params = "maxPrice", spec = LessThanOrEqual.class),
                                                                       @Spec(path = "dateOfPurchase", params = "start", spec = GreaterThanOrEqual.class),
                                                                       @Spec(path = "dateOfPurchase", params = "end", spec = LessThanOrEqual.class)})
                                                                       Specification<Transaction> transactionSpecification,
                                                                     @RequestParam(defaultValue = "false") Boolean groupingTransactionBack) {
    return ResponseEntity.ok(transactionController.getWalletTransactions(principal, pageable, transactionSpecification,groupingTransactionBack));
  }
}
