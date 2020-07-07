package pl.wallet.transaction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<? extends Transaction> findAll (Specification<Transaction> transactionSpecification, Pageable pageable);

  @Query("SELECT t FROM Transaction t INNER JOIN Wallet w ON t.wallet = w WHERE w.id = :walletId")
  List<? extends Transaction> getTransactionsByWalletId (@Param("walletId") Long walletId);

}
