package pl.wallet.transaction.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserService;
import pl.wallet.Wallet;
import pl.wallet.WalletService;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryService;
import pl.wallet.transaction.dto.TransactionRecurringDto;
import pl.wallet.transaction.mapper.TransactionRecurringMapper;
import pl.wallet.transaction.model.TransactionRecurring;
import pl.wallet.transaction.repository.TransactionRecurringRepository;

import java.security.Principal;

@Service
@AllArgsConstructor
public class TransactionRecurringService {

   private TransactionRecurringRepository transactionRecurringRepository;
   private UserService userService;
   private WalletService walletService;
   private CategoryService categoryService;

   public TransactionRecurringDto addTransactionRecurring(Principal principal, Long walletId, TransactionRecurringDto transactionRecurringDto) {
      User user = userService.getUser(principal);
      Wallet wallet = walletService.isUserWallet(principal.getName(), walletId);
      Category category = categoryService.getCategory(user, transactionRecurringDto.getTransaction().getCategoryId());
      TransactionRecurring transactionRecurring = TransactionRecurringMapper.toEntity(transactionRecurringDto, category);
      TransactionRecurring savedTransactionRecurring = this.save(transactionRecurring);
      wallet.addTransactionRecurring(transactionRecurring);
      walletService.save(wallet);
      return TransactionRecurringMapper.toDto(savedTransactionRecurring);
   }

   public TransactionRecurringDto editTransactionRecurring(Principal principal, Long walletId, Long transactionRecurringId, TransactionRecurringDto transactionRecurringDto) {
      TransactionRecurring transactionRecurring = this.getOne(principal.getName(), walletId, transactionRecurringId);
      Category category = categoryService.getCategory(principal.getName(), transactionRecurringDto.getTransaction().getCategoryId()).orElseThrow(ThereIsNoYourPropertyException::new);
      TransactionRecurring transactionRecurringWithNewValues = TransactionRecurringMapper.toEntity(transactionRecurringDto, category);
      TransactionRecurring editedTransactionRecurring = updateNotNullFields(transactionRecurringDto, transactionRecurringWithNewValues);
      return null;
   }

   //    TODO Finish this
   private TransactionRecurring updateNotNullFields(TransactionRecurringDto newTransactionRecurring, TransactionRecurring oldTransactionRecurring) {
      TransactionRecurring toUpdateTransactionRecurring = oldTransactionRecurring;
//        toUpdateTransactionRecurring.set
      return toUpdateTransactionRecurring;
   }

   public void removeTransactionRecurring(Principal principal, Long walletId, Long transactionRecurringId) {
      this.remove(this.getOne(principal.getName(), walletId, transactionRecurringId));
   }

   public TransactionRecurring save(TransactionRecurring transactionRecurring) {
      return transactionRecurringRepository.save(transactionRecurring);
   }

   private TransactionRecurring getOne(String email, Long walletId, Long transactionRecurringId) {
      return transactionRecurringRepository.get(email, walletId, transactionRecurringId).orElseThrow(ThereIsNoYourPropertyException::new);
   }

   public void remove(TransactionRecurring transactionRecurring) {
      transactionRecurringRepository.delete(transactionRecurring);
   }
}
