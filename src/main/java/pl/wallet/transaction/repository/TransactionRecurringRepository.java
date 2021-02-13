package pl.wallet.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.wallet.transaction.model.TransactionRecurring;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRecurringRepository extends JpaRepository<TransactionRecurring, Long> {

   @Query("SELECT t FROM User u INNER JOIN u.wallets w INNER JOIN w.transactionsRecurring t WHERE u.email = :email AND w.id = :walletId AND t.id = :transactionRecurringId")
   Optional<TransactionRecurring> get(@Param("email") String email, @Param("walletId") Long walletId, @Param("transactionRecurringId") Long transactionRecurringId);

   List<TransactionRecurring> getAll();

}
