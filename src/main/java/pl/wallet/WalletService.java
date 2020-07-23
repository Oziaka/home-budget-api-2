package pl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.EntityNotFoundException;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.wallet.transaction.model.Transaction;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

@Service
@AllArgsConstructor
public class WalletService {

    public static final String DEAFULT_WALLET_NAME = "Wallet";

    private WalletRepository walletRepository;

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    Set<Wallet> getWalletsByUser(User user) {
        return walletRepository.getByUsersIn(Collections.singleton(user));
    }

    void removeWallet(Long walletId) {
        walletRepository.deleteById(walletId);
    }

    Wallet getWalletByOwnerAndId(Long walletId) {
        return walletRepository.getById(walletId).orElseThrow(() -> new EntityNotFoundException(walletId, Wallet.class));
    }

    public Wallet isUserWallet(User user, Long walletId) {
        if (this.getWalletsByUser(user).stream().anyMatch(userWallet -> userWallet.getId().equals(walletId)))
            return getWalletByOwnerAndId(walletId);
        throw new ThereIsNoYourPropertyException();
    }


    public Wallet addTransaction(Wallet wallet, Transaction transaction) {
        wallet.addTransaction(transaction);
        return save(wallet);
    }

    public Wallet saveDefaultWallet(User user) {
        Wallet defaultWallet = createDefaultWallet();
        defaultWallet.setUsers(Collections.singleton(user));
        return save(defaultWallet);
    }

    private Wallet createDefaultWallet() {
        Wallet wallet = new Wallet();
        wallet.setName(DEAFULT_WALLET_NAME);
        wallet.setBalance(BigDecimal.ZERO);
        return wallet;
    }

    Wallet getWalletByOwnerAndId(User owner, Long walletId) {
        return walletRepository.getByIdAndOwner(walletId, owner).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    Wallet getWalletByUserAndId(User user, Long id) {
        return walletRepository.getByIdAndUsersIn(id, Collections.singleton(user)).orElseThrow(ThereIsNoYourPropertyException::new);
    }
}
