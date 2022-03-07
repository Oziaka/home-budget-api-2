package pl.wallet.currency;

import lombok.*;


@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class CurrencyDto {

    private Long id;
    private String country;
    private String currency;
    private String code;


    @Builder
    public CurrencyDto(Long id, String country, String currency, String code) {
        this.id = id;
        this.country = country;
        this.currency = currency;
        this.code = code;
    }
}
