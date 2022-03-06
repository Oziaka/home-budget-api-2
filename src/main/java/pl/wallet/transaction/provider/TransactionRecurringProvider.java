package pl.wallet.transaction.provider;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wallet.transaction.repository.TransactionRecurringRepository;

@Service
@AllArgsConstructor
public class TransactionRecurringProvider {
    private final TransactionRecurringRepository transactionRecurringRepository;

    public void remove(Long id) {
        transactionRecurringRepository.deleteById(id);
    }

}
