package pl.wallet.transaction;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.wallet.Wallet;
import pl.wallet.category.Category;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dType")
public class Transaction {

    @Column(name = "transaction_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(insertable = false, updatable = false)
    private String dType;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToOne
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    private Wallet wallet;

    @CreationTimestamp
    private LocalDateTime dateOfPurchase;


    @Builder
    public Transaction(String name, String description, Category category, BigDecimal price, Wallet wallet, LocalDateTime dateOfPurchase) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.wallet = wallet;
        this.dateOfPurchase = dateOfPurchase;
    }
}

