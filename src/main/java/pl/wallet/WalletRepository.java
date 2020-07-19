package pl.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> getByUser(User user);

    Optional<Wallet> getById(Long id);

    Optional<Wallet> getByIdAndUser(Long walletId, User user);
}
