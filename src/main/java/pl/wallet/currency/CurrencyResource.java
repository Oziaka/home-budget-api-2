package pl.wallet.currency;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/currency")
public class CurrencyResource {
    private CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getCurrencies(Principal principal) {
        return ResponseEntity.ok(currencyService.getAll());
    }
}
