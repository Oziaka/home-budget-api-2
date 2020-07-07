package pl.wallet.transaction;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wallet.Wallet;
import pl.wallet.category.Category;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class TransactionLoanOrBorrow extends Transaction {
  @OneToMany
  private List<TransactionBack> transactionsBack;
  private Boolean isFinished;

  @Builder(builderMethodName = "transactionLoanOrBorrowBuilder")
  public TransactionLoanOrBorrow (String name, String description, Category category, BigDecimal price, Wallet wallet, LocalDateTime dateOfPurchase, List<TransactionBack> transactionsBack, Boolean isFinished) {
    super(name, description, category, price, wallet, dateOfPurchase);
    this.transactionsBack = transactionsBack;
    this.isFinished = isFinished;
  }

  public void addTransactionsBack (TransactionBack transactionBack) {
    if(transactionBack == null)
      this.transactionsBack = new ArrayList<>();
    transactionsBack.add(transactionBack);
  }
}
