package pl.wallet.transaction.model;

import lombok.*;
import pl.wallet.Wallet;
import pl.wallet.transaction.enums.Frequency;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @OneToOne
    private Transaction transaction;

    @Enumerated
    private Frequency frequency;

    @ManyToOne
    private Wallet wallet;

    @Column(nullable = false)
    private LocalDateTime start;

    private LocalDateTime end;

    private LocalDateTime dateOfLastAdding;

    private LocalDateTime dateOfNextAdding;

    private Long numberOfRepetition;

    private Long day;

    @Builder
    public TransactionRecurring(Long id, Transaction transaction, Frequency frequency, Wallet wallet, LocalDateTime start, LocalDateTime end, LocalDateTime dateOfLastAdding, LocalDateTime dateOfNextAdding, Long numberOfRepetition, Long day) {
        this.id = id;
        this.transaction = transaction;
        this.frequency = frequency;
        this.wallet = wallet;
        this.start = start;
        this.end = end;
        this.dateOfLastAdding = dateOfLastAdding;
        this.dateOfNextAdding = dateOfNextAdding;
        this.numberOfRepetition = numberOfRepetition;
        this.day = day;
    }
}
