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

    Set<Wallet> getAll(User user) {
        return walletRepository.getByUsersIn(Collections.singleton(user));
    }

    void remove(Long walletId) {
        walletRepository.deleteById(walletId);
    }

    public Wallet isUserWallet(String email, Long walletId) {
        return walletRepository.getByIdAndUserEmail(walletId, email).orElseThrow(ThereIsNoYourPropertyException::new);
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

    Wallet getOneByOwner(User owner, Long walletId) {
        return walletRepository.getByIdAndOwner(walletId, owner).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    Wallet getOne(User user, Long id) {
        return walletRepository.getByIdAndUserEmail(id, user.getEmail()).orElseThrow(ThereIsNoYourPropertyException::new);
    }
}
