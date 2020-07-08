package pl.wallet.transaction;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import pl.exception.ReferenceTransactionMustBeLoanOrBorrowTransactionException;
import pl.exception.TransactionBackMustHaveBackTransactionOfReferenceTransactionTypeException;
import pl.user.User;
import pl.user.UserService;
import pl.wallet.Wallet;
import pl.wallet.WalletService;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class TransactionController {
  private TransactionService transactionService;
  private UserService userService;
  private WalletService walletService;
  private CategoryService categoryService;


  TransactionDto addTransaction (Principal principal, Long walletId, TransactionDto transactionDto) {
    User user = userService.getUserByEmail(principal.getName());
    Wallet wallet = getWallet(user, walletId);
    Category category = getCategory(user, transactionDto.getCategoryId());
    Transaction transaction = TransactionMapper.toEntity(transactionDto, category);
    transaction.setWallet(wallet);
    if(transaction instanceof TransactionBack)
      return addTransactionBack(walletId, transactionDto, category, transaction);
    else
      return addLoanOrBorrowOrSimpleTransaction(wallet, category, transaction);
  }

  private TransactionDto addLoanOrBorrowOrSimpleTransaction (Wallet wallet, Category category, Transaction transaction) {
    transaction.setWallet(wallet);
    transaction.setCategory(category);
    walletService.addTransaction(wallet, transaction);
    transaction = transactionService.save(transaction);
    return TransactionMapper.toDto(transaction);
  }

  private TransactionDto addTransactionBack (Long walletId, TransactionDto transactionDto, Category category, Transaction transaction) {
    Transaction referenceTransaction = transactionService.getTransaction(walletId, transactionDto.getTransactionIdReference());
    if(referenceTransaction instanceof TransactionLoanOrBorrow) {
      TransactionLoanOrBorrow transactionLoanOrBorrow = (TransactionLoanOrBorrow) referenceTransaction;
      Category transactionLoanOrBorrowCategory = transactionLoanOrBorrow.getCategory();
      if(correctTransactionType(category, transactionLoanOrBorrowCategory)) {
        TransactionBack savedTransactionBack = saveTransactionBack((TransactionBack) transaction, transactionLoanOrBorrow);
        return TransactionMapper.toDto(savedTransactionBack);
      } else {
        throw new TransactionBackMustHaveBackTransactionOfReferenceTransactionTypeException();
      }
    } else {
      throw new ReferenceTransactionMustBeLoanOrBorrowTransactionException();
    }
  }

  private TransactionBack saveTransactionBack (TransactionBack transaction, TransactionLoanOrBorrow transactionLoanOrBorrow) {
    TransactionBack transactionBack = transaction;
    transactionBack.setTransactionLoanOrBorrow(transactionLoanOrBorrow);
    TransactionBack savedTransactionBack = (TransactionBack) transactionService.save(transactionBack);
    transactionLoanOrBorrow.addTransactionsBack(savedTransactionBack);
    transactionService.save(transactionLoanOrBorrow);
    return savedTransactionBack;
  }

  private boolean correctTransactionType (Category category, Category transactionLoanOrBorrowCategory) {
    return (transactionLoanOrBorrowCategory.getTransactionType() == TransactionType.LOAN && category.getTransactionType() == TransactionType.LOAN_BACK) || (transactionLoanOrBorrowCategory.getTransactionType() == TransactionType.BORROW && category.getTransactionType() == TransactionType.BORROW_BACK);
  }

  private Category getCategory (User user, Long categoryId) {
    return categoryService.getCategory(user, categoryId);
  }

  private Wallet getWallet (User user, Long walletId) {
    return walletService.isUserWallet(user, walletId);
  }

  void removeTransaction (Principal principal, Long walletId, Long transactionId) {
    User user = userService.getUserByEmail(principal.getName());
    Wallet wallet = getWallet(user, walletId);
    Transaction transaction = transactionService.getTransaction(walletId, transactionId);
    wallet.removeTransaction(transaction);
    transactionService.removeTransaction(transactionId);
    walletService.saveWallet(wallet);
  }

  List<TransactionDto> getWalletTransactions (Principal principal, Pageable pageable, Specification<Transaction> transactionSpecification) {
    User user = userService.getUser(principal);
    transactionSpecification.and(new UserWallet(user));
    transactionSpecification.and(new NotTransactionBack("TransactionBack"));
    return transactionService.getTransactionsByWalletId(pageable, transactionSpecification).stream().map(TransactionMapper::toDto).collect(Collectors.toList());
  }

  TransactionDto getTransaction (Principal principal, Long walletId, Long transactionId) {
    User user = userService.getUser(principal);
    walletService.isUserWallet(user, walletId);
    return TransactionMapper.toDto(transactionService.getTransaction(walletId, transactionId));
  }

  TransactionDto editTransaction (Principal principal, Long walletId, Long transactionId, TransactionDto transactionDto) {
    User user = userService.getUser(principal);
    walletService.isUserWallet(user, walletId);
    Transaction transaction = transactionService.getTransaction(walletId, transactionId);
    updateNotNullTransactionDtoValuesInTransaction(transactionDto, transaction);
    transactionService.save(transaction);
    return TransactionMapper.toDto(transaction);
  }

  private void updateNotNullTransactionDtoValuesInTransaction (TransactionDto transactionDto, Transaction transaction) {
    if(transactionDto.getDescription() != null) transaction.setDescription(transactionDto.getDescription());
    if(transactionDto.getPrice() != null) transaction.setPrice(transactionDto.getPrice());
    if(transactionDto.getDateOfPurchase() != null) transaction.setDateOfPurchase(transactionDto.getDateOfPurchase());
    if(transactionDto.getName() != null) transaction.setName(transactionDto.getName());
  }
}
