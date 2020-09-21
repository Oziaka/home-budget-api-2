package pl.wallet;

import lombok.*;
import pl.user.User;
import pl.wallet.transaction.model.Transaction;
import pl.wallet.transaction.model.TransactionRecurring;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
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

   @OneToMany(mappedBy = "wallet")
   private List<TransactionRecurring> transactionsRecurring;


   public void addTransaction(Transaction transaction) {
      this.balance = transaction.getCategory().getType().countBalance(this, transaction);
   }

   public void removeTransaction(Transaction transaction) {
      this.balance = transaction.getCategory().getType().undoCountBalance(this, transaction);
   }

   public void addUser(User user) {
      if (users == null)
         users = new HashSet<>(Set.of(user));
      users.add(user);
   }

   public void removeUser(User user) {
      this.users = this.users.stream().filter(u -> !u.equals(user)).collect(Collectors.toSet());
   }

   public void addTransactionRecurring(TransactionRecurring transactionRecurring) {
      if (transactionsRecurring == null)
         transactionsRecurring = new ArrayList<>();
      this.transactionsRecurring.add(transactionRecurring);
   }

   public void removeTransactionRecurring(TransactionRecurring transactionRecurring) {
      this.transactionsRecurring = transactionsRecurring.stream().filter(t -> t.equals(transactionRecurring)).collect(Collectors.toList());
   }

   @Builder
   public Wallet(String name, BigDecimal balance, List<Transaction> transactions, Set<User> users, User owner, List<TransactionRecurring> transactionsRecurring) {
      this.name = name;
      this.balance = balance;
      this.transactions = transactions;
      this.users = users;
      this.owner = owner;
      this.transactionsRecurring = transactionsRecurring;
   }
}
