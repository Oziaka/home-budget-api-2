package pl.wallet.transaction.model;

import lombok.*;
import pl.wallet.transaction.enums.Frequency;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction_recurring")
public class TransactionRecurring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_recurring_id")
    private Long id;

    @OneToMany
    @JoinColumn(name = "transaction_id")
    private Set<Transaction> transactions;

    @Enumerated
    private Frequency frequency;

    @Column(nullable = false)
    private LocalDateTime start;

    private LocalDateTime end;

    private LocalDateTime dateOfLastAdding;

    private LocalDateTime dateOfNextAdding;

    private Long numberOfRepetition;

    private Long currentNumberRepetitions;

    public void addTransaction(Transaction transaction) {
        if (transactions == null) transactions = new HashSet<>();
        transactions.add(transaction);
    }
}
