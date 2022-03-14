package pl.wallet.currency;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrencyService {
    private CurrencyRepository currencyRepository;

    public List<CurrencyDto> getAll() {
        return currencyRepository.findAll().stream().map(CurrencyMapper::toDto).collect(Collectors.toList());
    }
}
