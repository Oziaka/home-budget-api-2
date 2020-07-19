package pl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.EntityNotFoundException;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.wallet.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private WalletRepository walletRepository;

    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    List<Wallet> getWalletsByUser(User user) {
        return walletRepository.getByUser(user);
    }

    void removeWallet(Long walletId) {
        walletRepository.deleteById(walletId);
    }

    Wallet getUserWallet(Long walletId) {
        return walletRepository.getById(walletId).orElseThrow(() -> new EntityNotFoundException(walletId, Wallet.class));
    }

    public Wallet isUserWallet(User user, Long walletId) {
        if (this.getWalletsByUser(user).stream().anyMatch(userWallet -> userWallet.getId().equals(walletId)))
            return getUserWallet(walletId);
        throw new ThereIsNoYourPropertyException();
    }


    public Wallet addTransaction(Wallet wallet, Transaction transaction) {
        wallet.addTransaction(transaction);
        return saveWallet(wallet);
    }

    public Wallet saveDefaultWallet(User user) {
        Wallet defaultWallet = createDefaultWallet();
        defaultWallet.setUser(user);
        return saveWallet(defaultWallet);
    }

    private Wallet createDefaultWallet() {
        Wallet wallet = new Wallet();
        wallet.setName("Wallet");
        wallet.setBalance(BigDecimal.ZERO);
        return wallet;
    }

    public Optional<Wallet> getUserWallet(User user, Long walletId) {
        return walletRepository.getByIdAndUser(walletId, user);
    }

}
