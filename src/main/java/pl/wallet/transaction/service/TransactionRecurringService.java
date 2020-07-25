package pl.wallet.transaction.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.wallet.transaction.model.TransactionRecurring;
import pl.wallet.transaction.repository.TransactionRecurringRepository;

@Service
@AllArgsConstructor
public class TransactionRecurringService {

    private TransactionRecurringRepository transactionRecurringRepository;

    public TransactionRecurring save(TransactionRecurring transactionRecurring) {
        return transactionRecurringRepository.save(transactionRecurring);
    }

    public TransactionRecurring getOne(String email, Long walletId, Long transactionRecurringId) {
        return transactionRecurringRepository.get(email, walletId, transactionRecurringId).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    public void remove(TransactionRecurring transactionRecurring) {
        transactionRecurringRepository.delete(transactionRecurring);
    }
}
