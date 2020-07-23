package pl.wallet.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.wallet.transaction.enums.Frequency;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Table(name = "transaction_recurring")
public class TransactionRecurring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "transaction_id")
    private Set<? extends Transaction> transactions;

    @Enumerated
    private Frequency frequency;

    @Column(nullable = false)
    private LocalDateTime start;

    private LocalDateTime finish;

    private Long numberOfRepetition;

    private Long currentNumberRepetitions;

}
