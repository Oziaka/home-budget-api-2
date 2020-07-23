package pl.wallet.transaction.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.wallet.transaction.repository.TransactionRepository;
import pl.wallet.transaction.model.Transaction;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(Long walletId, Long transactionId) {
        return transactionRepository.findByIdAndWallet_Id(transactionId, walletId).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    public List<? extends Transaction> getTransactionsByWalletId(Pageable pageable, Specification<Transaction> transactionSpecification) {
        return transactionRepository.findAll(transactionSpecification, pageable);
    }

    public void removeTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public void removeWalletTransactions(Long walletId) {
        transactionRepository.getTransactionsByWalletId(walletId).forEach(transaction -> this.removeTransaction(transaction.getId()));
    }
}
