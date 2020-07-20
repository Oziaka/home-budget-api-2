package pl.wallet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.user.User;
import pl.wallet.transaction.Transaction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "wallet")
public class Wallet {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;

    @ManyToMany(mappedBy = "wallets")
    private Set<User> users;

    public void addTransaction(Transaction transaction) {
        this.balance = transaction.getCategory().getTransactionType().countBalance(this, transaction);
    }

    public void removeTransaction(Transaction transaction) {
        this.balance = transaction.getCategory().getTransactionType().undoCountBalance(this, transaction);
    }
}
