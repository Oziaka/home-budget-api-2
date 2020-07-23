package pl.wallet.transaction.controller;

import org.springframework.stereotype.Controller;
import pl.user.User;
import pl.user.UserService;
import pl.wallet.Wallet;
import pl.wallet.WalletService;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryService;
import pl.wallet.transaction.dto.TransactionDto;
import pl.wallet.transaction.dto.TransactionRecurringDto;
import pl.wallet.transaction.mapper.TransactionMapper;
import pl.wallet.transaction.mapper.TransactionRecurringMapper;
import pl.wallet.transaction.model.Transaction;
import pl.wallet.transaction.model.TransactionRecurring;
import pl.wallet.transaction.service.TransactionRecurringService;
import pl.wallet.transaction.service.TransactionService;

import java.security.Principal;

@Controller
public class TransactionRecurringController {

    private TransactionRecurringService transactionRecurringService;
    private UserService userService;
    private WalletService walletService;
    private CategoryService categoryService;
    private TransactionService transactionService;

    public TransactionRecurringDto addTransactionRecurring(Principal principal, Long walletId, TransactionRecurringDto transactionRecurringDto) {
        User user = userService.getUser(principal);
        Wallet wallet = walletService.isUserWallet(user, walletId);
        TransactionRecurring transactionRecurring = TransactionRecurringMapper.toEntity(transactionRecurringDto);
        TransactionRecurring savedTransactionRecurring = transactionRecurringService.save(transactionRecurring);
        wallet.addTransactionRecurring(transactionRecurring);
        walletService.save(wallet);
        return TransactionRecurringMapper.toDto(savedTransactionRecurring);
    }

    public TransactionRecurringDto addTransactionToTransactionRecurring(Principal principal, Long walletId, Long transactionRecurringId, TransactionDto transactionDto) {
        User user = userService.getUser(principal);
        Wallet wallet = walletService.isUserWallet(user, walletId);
        TransactionRecurring transactionRecurring = transactionRecurringService.get(walletId, transactionRecurringId);
        Category category = categoryService.getCategory(user, transactionDto.getCategoryId());
        Transaction transaction = TransactionMapper.toEntity(transactionDto, category);
        Transaction savedTransaction = transactionService.save(transaction);
        transactionRecurring.addTransaction(savedTransaction);
        TransactionRecurring savedTransactionRecurring = transactionRecurringService.save(transactionRecurring);
        return TransactionRecurringMapper.toDto(savedTransactionRecurring);
    }
}
