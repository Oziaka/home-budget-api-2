package pl.wallet.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wallet.transaction.model.TransactionRecurring;

@Repository
public interface TransactionRecurringRepository extends JpaRepository<TransactionRecurring, Long> {
}
