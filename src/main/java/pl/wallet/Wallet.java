package pl.wallet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.user.User;
import pl.wallet.transaction.model.Transaction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @OneToOne
    private User owner;

    public void addTransaction(Transaction transaction) {
        this.balance = transaction.getCategory().getType().countBalance(this, transaction);
    }

    public void removeTransaction(Transaction transaction) {
        this.balance = transaction.getCategory().getType().undoCountBalance(this, transaction);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user){
        this.users = this.users.stream().filter(u->u.equals(user)).collect(Collectors.toSet());
    }
}
