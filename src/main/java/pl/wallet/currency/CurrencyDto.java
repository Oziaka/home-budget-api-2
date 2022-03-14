package pl.wallet.currency;

import lombok.*;


@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class CurrencyDto {

    private Long id;
    private String stateOrTerritory;
    private String currency;
    private String code;
    private String symbol;


    @Builder
    public CurrencyDto(Long id, String stateOrTerritory, String currency, String code, String symbol) {
        this.id = id;
        this.stateOrTerritory = stateOrTerritory;
        this.currency = currency;
        this.code = code;
        this.symbol = symbol;
    }
}
