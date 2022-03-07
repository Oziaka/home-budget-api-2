package pl.wallet.currency;

public class CurrencyMapper {

    public static CurrencyDto toDto(Currency currency) {
        return CurrencyDto.builder().id(currency.getId()).country(currency.getCountry()).currency(currency.getCurrency()).code(currency.getCode()).build();
    }

    public static Currency toEntity(CurrencyDto currencyDto) {
        return Currency.builder().id(currencyDto.getId()).country(currencyDto.getCountry()).currency(currencyDto.getCurrency()).code(currencyDto.getCode()).build();
    }
}
