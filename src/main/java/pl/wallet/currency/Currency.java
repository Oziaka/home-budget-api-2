package pl.wallet.currency;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "currency")
@ToString
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Long id;
    private String country;
    private String currency;
    private String code;

    @Builder
    public Currency(Long id, String country, String currency, String code) {
        this.id = id;
        this.country = country;
        this.currency = currency;
        this.code = code;
    }
}
