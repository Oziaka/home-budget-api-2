package pl.wallet.transaction.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.user.User;
import pl.user.UserProvider;
import pl.wallet.Wallet;
import pl.wallet.WalletProvider;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryService;
import pl.wallet.transaction.dto.TransactionDto;
import pl.wallet.transaction.enums.Type;
import pl.wallet.transaction.exception.TransactionException;
import pl.wallet.transaction.exception.TransactionError;
import pl.wallet.transaction.mapper.TransactionMapper;
import pl.wallet.transaction.model.Transaction;
import pl.wallet.transaction.model.TransactionBack;
import pl.wallet.transaction.model.TransactionLoanOrBorrow;
import pl.wallet.transaction.repository.TransactionRepository;
import pl.wallet.transaction.specification.IsUserWallet;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserProvider userProvider;
    private final WalletProvider walletProvider;
    private final CategoryService categoryService;

    public TransactionDto addTransaction(Principal principal, Long walletId, TransactionDto transactionDto) {
        Wallet wallet = getWallet(principal.getName(), walletId);
        Category category = categoryService.getCategory(principal.getName(), transactionDto.getCategoryId()).orElseThrow(()->new TransactionException(TransactionError.NO_YOUR_PROPERTY));
        Transaction transaction = TransactionMapper.toEntity(transactionDto, category);
        transaction.setWallet(wallet);
        if (transaction instanceof TransactionBack)
            return addTransactionBack(principal, walletId, transactionDto, category, transaction);
        else
            return addLoanOrBorrowOrSimpleTransaction(wallet, category, transaction);
    }

    public List<TransactionDto> getWalletTransactions(Principal principal, Pageable pageable, Specification<Transaction> transactionSpecification, Boolean groupingTransactionBack) {
        User user = userProvider.get(principal);
        transactionSpecification.and(new IsUserWallet(user));
        List<? extends Transaction> transactions = this.getAll(pageable, transactionSpecification);
        return transactions.stream().filter(transaction -> groupingTransactionBack ? !(transaction instanceof TransactionBack) : true).map(groupingTransactionBack ? TransactionMapper::toDto : TransactionMapper::toDtoAndTransactionLoanOrBorrowWithoutTransactionsBack).collect(Collectors.toList());

    }

    public TransactionDto getTransaction(Principal principal, Long walletId, Long transactionId) {
        return TransactionMapper.toDto(this.get(principal.getName(), walletId, transactionId));
    }

    public TransactionDto editTransaction(Principal principal, Long walletId, Long transactionId, TransactionDto transactionDto) {
        walletProvider.get(principal.getName(), walletId);
        Transaction transaction = this.get(principal.getName(), walletId, transactionId);
        updateTransactionFromNotNullFieldsInTransactionDto(transactionDto, transaction);
        this.save(transaction);
        return TransactionMapper.toDto(transaction);
    }

    private TransactionDto addLoanOrBorrowOrSimpleTransaction(Wallet wallet, Category category, Transaction transaction) {
        transaction.setWallet(wallet);
        transaction.setCategory(category);
        walletProvider.addTransaction(wallet, transaction);
        transaction = this.save(transaction);
        return TransactionMapper.toDto(transaction);
    }

    private TransactionDto addTransactionBack(Principal principal, Long walletId, TransactionDto transactionDto, Category category, Transaction transaction) {
        Transaction referenceTransaction = this.get(principal.getName(), walletId, transactionDto.getTransactionIdReference());
        if (referenceTransaction instanceof TransactionLoanOrBorrow) {
            TransactionLoanOrBorrow transactionLoanOrBorrow = (TransactionLoanOrBorrow) referenceTransaction;
            Category transactionLoanOrBorrowCategory = transactionLoanOrBorrow.getCategory();
            if (correctTransactionType(category, transactionLoanOrBorrowCategory)) {
                TransactionBack savedTransactionBack = saveTransactionBack((TransactionBack) transaction, transactionLoanOrBorrow);
                return TransactionMapper.toDto(savedTransactionBack);
            } else {
                throw new TransactionException(TransactionError.INVALID_TRANSACTION);
            }
        } else {
            throw new TransactionException(TransactionError.INVALID_TRANSACTION);
        }
    }

    private TransactionBack saveTransactionBack(TransactionBack transaction, TransactionLoanOrBorrow transactionLoanOrBorrow) {
        transaction.setTransactionLoanOrBorrow(transactionLoanOrBorrow);
        TransactionBack savedTransactionBack = (TransactionBack) this.save(transaction);
        transactionLoanOrBorrow.addTransactionsBack(savedTransactionBack);
        this.save(transactionLoanOrBorrow);
        return savedTransactionBack;
    }

    private boolean correctTransactionType(Category category, Category transactionLoanOrBorrowCategory) {
        return (transactionLoanOrBorrowCategory.getType() == Type.LOAN && category.getType() == Type.LOAN_BACK) || (transactionLoanOrBorrowCategory.getType() == Type.BORROW && category.getType() == Type.BORROW_BACK);
    }


    private Wallet getWallet(String email, Long walletId) {
        return walletProvider.get(email, walletId).orElseThrow(()->new TransactionException(TransactionError.NO_YOUR_PROPERTY));
    }

    public void removeTransaction(Principal principal, Long walletId, Long transactionId) {
        Wallet wallet = getWallet(principal.getName(), walletId);
        Transaction transaction = this.get(principal.getName(), walletId, transactionId);
        wallet.removeTransaction(transaction);
        this.remove(transactionId);
        walletProvider.save(wallet);
    }

    private void updateTransactionFromNotNullFieldsInTransactionDto(TransactionDto transactionDto, Transaction transaction) {
        if (transactionDto.getDescription() != null) transaction.setDescription(transactionDto.getDescription());
        if (transactionDto.getPrice() != null) transaction.setPrice(transactionDto.getPrice());
        if (transactionDto.getDateOfPurchase() != null)
            transaction.setDateOfPurchase(transactionDto.getDateOfPurchase());
        if (transactionDto.getName() != null) transaction.setName(transactionDto.getName());
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction get(String email, Long walletId, Long transactionId) {
        return transactionRepository.findByuEmailAndWalletIdAndTransactionId(email, transactionId, walletId).orElseThrow(()->new TransactionException(TransactionError.NO_YOUR_PROPERTY));
    }

    private List<? extends Transaction> getAll(Pageable pageable, Specification<Transaction> transactionSpecification) {
        return transactionRepository.findAll(transactionSpecification, pageable);
    }

    public void remove(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }


}
