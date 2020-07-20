package pl.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Set<Wallet> getByUsersIn(Set<User> users);

    Optional<Wallet> getById(Long id);

    Optional<Wallet> getByIdAndUsersIn(Long walletId, Set<User> users);
}
