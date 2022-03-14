package pl.wallet.currency;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Long id;
    private String StateOrTerritory;
    private String currency;
    private String code;
    private String symbol;

    @Builder
    public Currency(Long id, String stateOrTerritory, String currency, String code, String symbol) {
        this.id = id;
        this.StateOrTerritory = stateOrTerritory;
        this.currency = currency;
        this.code = code;
        this.symbol = symbol;
    }
}
