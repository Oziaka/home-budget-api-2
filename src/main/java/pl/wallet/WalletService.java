package pl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.wallet.transaction.model.Transaction;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

@Service
@AllArgsConstructor
public class WalletService {

   private WalletRepository walletRepository;

   public Wallet save(Wallet wallet) {
      return walletRepository.save(wallet);
   }

   Set<Wallet> getAll(User user) {
      return walletRepository.getByUser(user);
   }

   void remove(Long walletId) {
      walletRepository.deleteById(walletId);
   }

   public Wallet isUserWallet(String email, Long walletId) {
      return walletRepository.findByIdAndUserEmail(walletId, email).orElseThrow(ThereIsNoYourPropertyException::new);
   }


   public Wallet addTransaction(Wallet wallet, Transaction transaction) {
      wallet.addTransaction(transaction);
      return save(wallet);
   }


   Wallet getOneByOwner(User owner, Long walletId) {
      return walletRepository.findByIdAndOwner(walletId, owner).orElseThrow(ThereIsNoYourPropertyException::new);
   }

   Wallet getOne(User user, Long id) {
      return walletRepository.findByIdAndUserEmail(id, user.getEmail()).orElseThrow(ThereIsNoYourPropertyException::new);
   }
}
