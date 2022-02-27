package pl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wallet.transaction.model.Transaction;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletProvider {

    private final WalletRepository walletRepository;

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Optional<Wallet> get(String email, Long walletId) {
        return walletRepository.findByIdAndUserEmail(walletId, email);
    }

    public Wallet addTransaction(Wallet wallet, Transaction transaction) {
        wallet.addTransaction(transaction);
        return save(wallet);
    }
}
