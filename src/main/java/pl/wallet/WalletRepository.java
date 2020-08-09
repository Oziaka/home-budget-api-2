package pl.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

   @Query("SELECT DISTINCT w FROM Wallet w INNER JOIN w.users u WHERE u = :user")
   Set<Wallet> getByUser(@Param("user") User user);

   @Query("SELECT w FROM Wallet w INNER JOIN w.users u WHERE w.id = :walletId AND u.email = :email")
   Optional<Wallet> findByIdAndUserEmail(@Param("walletId") Long walletId, @Param("email") String email);

   Optional<Wallet> findByIdAndOwner(Long walletId, User owner);
}
