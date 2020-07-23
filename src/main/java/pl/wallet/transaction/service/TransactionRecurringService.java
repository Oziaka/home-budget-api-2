package pl.wallet.transaction.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wallet.transaction.repository.TransactionRecurringRepository;

@Service
@AllArgsConstructor
public class TransactionRecurringService {

    private TransactionRecurringRepository transactionRecurringRepository;
}
