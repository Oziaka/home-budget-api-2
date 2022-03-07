package pl.wallet.currency;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrencyService {
    private CurrencyRepository currencyRepository;
}
