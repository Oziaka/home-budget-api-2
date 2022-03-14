package pl.wallet.currency;

public class CurrencyMapper {

    public static CurrencyDto toDto(Currency currency) {
        return CurrencyDto.builder().id(currency.getId()).stateOrTerritory(currency.getStateOrTerritory()).currency(currency.getCurrency()).code(currency.getCode()).symbol(currency.getSymbol()).build();
    }

    public static Currency toEntity(CurrencyDto currencyDto) {
        return Currency.builder().id(currencyDto.getId()).stateOrTerritory(currencyDto.getStateOrTerritory()).currency(currencyDto.getCurrency()).code(currencyDto.getCode()).symbol(currencyDto.getSymbol()).build();
    }
}
