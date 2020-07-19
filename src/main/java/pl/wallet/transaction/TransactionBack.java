package pl.wallet.transaction;

import lombok.*;
import pl.wallet.Wallet;
import pl.wallet.category.Category;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class TransactionBack extends Transaction {

    @ManyToOne
    private TransactionLoanOrBorrow transactionLoanOrBorrow;

    @Builder(builderMethodName = "transactionBackBuilder")
    public TransactionBack(String name, String description, Category category, BigDecimal price, Wallet wallet, LocalDateTime dateOfPurchase, TransactionLoanOrBorrow transactionLoanOrBorrow) {
        super(name, description, category, price, wallet, dateOfPurchase);
        this.transactionLoanOrBorrow = transactionLoanOrBorrow;
    }
}
