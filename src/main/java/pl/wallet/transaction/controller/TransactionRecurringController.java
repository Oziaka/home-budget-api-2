package pl.wallet.transaction.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.user.User;
import pl.user.UserService;
import pl.wallet.Wallet;
import pl.wallet.WalletService;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryService;
import pl.wallet.transaction.dto.TransactionRecurringDto;
import pl.wallet.transaction.mapper.TransactionRecurringMapper;
import pl.wallet.transaction.model.TransactionRecurring;
import pl.wallet.transaction.service.TransactionRecurringService;
import pl.wallet.transaction.service.TransactionService;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class TransactionRecurringController {

   private TransactionRecurringService transactionRecurringService;
   private UserService userService;
   private WalletService walletService;
   private CategoryService categoryService;
   private TransactionService transactionService;

   public TransactionRecurringDto addTransactionRecurring(Principal principal, Long walletId, TransactionRecurringDto transactionRecurringDto) {
      User user = userService.get(principal);
      Wallet wallet = walletService.isUserWallet(principal.getName(), walletId);
      Category category = categoryService.get(user, transactionRecurringDto.getTransaction().getCategoryId());
      TransactionRecurring transactionRecurring = TransactionRecurringMapper.toEntity(transactionRecurringDto, category);
      TransactionRecurring savedTransactionRecurring = transactionRecurringService.save(transactionRecurring);
      wallet.addTransactionRecurring(transactionRecurring);
      walletService.save(wallet);
      return TransactionRecurringMapper.toDto(savedTransactionRecurring);
   }

   public TransactionRecurringDto editTransactionRecurring(Principal principal, Long walletId, Long transactionRecurringId, TransactionRecurringDto transactionRecurringDto) {
      TransactionRecurring transactionRecurring = transactionRecurringService.getOne(principal.getName(), walletId, transactionRecurringId);
      Category category = categoryService.get(principal.getName(), transactionRecurringDto.getTransaction().getCategoryId());
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
      transactionRecurringService.remove(transactionRecurringService.getOne(principal.getName(), walletId, transactionRecurringId));
   }
}
