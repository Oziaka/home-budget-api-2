package pl.wallet.currency;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
public class CurrencyResource {
    private CurrencyService currencyService;
}
