package pl.wallet.transaction.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.wallet.transaction.model.Transaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<? extends Transaction> findAll(Specification<Transaction> transactionSpecification, Pageable pageable);

    @Query("SELECT t FROM Transaction t INNER JOIN Wallet w ON t.wallet = w WHERE w.id = :walletId")
    List<? extends Transaction> getTransactionsByWalletId(@Param("walletId") Long walletId);

    @Query("SELECT t FROM User u INNER JOIN u.wallets w INNER JOIN w.transactions t WHERE u.email = :email AND w.id = :walletId AND t.id = :transactionId")
    Optional<? extends Transaction> findByuEmailAndWalletIdAndTransactionId(@Param("email") String email, @Param("walletId") Long walletId, @Param("transactionId") Long transactionIds);
}
