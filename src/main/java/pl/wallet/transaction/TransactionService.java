package pl.wallet.transaction;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

  private TransactionRepository transactionRepository;

  Transaction save (Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  Transaction getTransaction (Long walletId, Long transactionId) {
    return transactionRepository.findByIdAndWallet_Id(transactionId, walletId).orElseThrow(ThereIsNoYourPropertyException::new);
  }

  List<? extends Transaction> getTransactionsByWalletId (Pageable pageable, Specification<Transaction> transactionSpecification) {
    return transactionRepository.findAll(transactionSpecification, pageable);
  }

  void removeTransaction (Long transactionId) {
    transactionRepository.deleteById(transactionId);
  }

  public void removeWalletTransactions (Long walletId) {
    transactionRepository.getTransactionsByWalletId(walletId).forEach(transaction -> this.removeTransaction(transaction.getId()));
  }
}
