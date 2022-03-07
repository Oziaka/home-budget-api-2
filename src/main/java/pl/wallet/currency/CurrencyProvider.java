package pl.wallet.currency;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrencyProvider {
    private CurrencyRepository currencyRepository;
}
