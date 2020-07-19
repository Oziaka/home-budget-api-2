package pl.wallet.transaction;

import pl.exception.InvalidTransactionException;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class TransactionMapper {

    static Transaction toEntity(TransactionDto transactionDto, Category category) {
        if (isTransactionBack(transactionDto, category))
            return TransactionBack.transactionBackBuilder()
                    .name(transactionDto.getName())
                    .description(transactionDto.getDescription())
                    .price(transactionDto.getPrice())
                    .dateOfPurchase(transactionDto.getDateOfPurchase())
                    .category(category)
                    .build();
        else if ((isLoanOrBorrowTransaction(transactionDto, category)))
            return TransactionLoanOrBorrow.transactionLoanOrBorrowBuilder()
                    .name(transactionDto.getName())
                    .description(transactionDto.getDescription())
                    .price(transactionDto.getPrice())
                    .dateOfPurchase(transactionDto.getDateOfPurchase())
                    .category(category)
                    .build();
        else if (isSimpleTransaction(transactionDto, category))
            return Transaction.builder()
                    .name(transactionDto.getName())
                    .description(transactionDto.getDescription())
                    .price(transactionDto.getPrice())
                    .dateOfPurchase(transactionDto.getDateOfPurchase())
                    .category(category)
                    .build();
        else throw new InvalidTransactionException();
    }

    static TransactionDto toDto(Transaction transaction) {
        if (transaction instanceof TransactionLoanOrBorrow) {
            return buildTransactionLoanOrBorrow((TransactionLoanOrBorrow) transaction).build();
        }
        if (transaction instanceof TransactionBack) {
            return buildTransactionBack((TransactionBack) transaction)
                    .build();
        }
        return buildTransaction(transaction)
                .build();
    }

    static TransactionDto toDtoAndTransactionLoanOrBorrowWithoutTransactionsBack(Transaction transaction) {
        if (transaction instanceof TransactionLoanOrBorrow) {
            return buildTransaction(transaction).build();
        }
        if (transaction instanceof TransactionBack) {
            return buildTransactionBack((TransactionBack) transaction)
                    .build();
        }
        return buildTransaction(transaction)
                .build();
    }

    private static TransactionDto.TransactionDtoBuilder buildTransactionLoanOrBorrow(TransactionLoanOrBorrow transactionLoanOrBorrow) {
        List<TransactionDto> collect = new ArrayList<>();
        if (transactionLoanOrBorrow.getTransactionsBack() != null)
            collect = transactionLoanOrBorrow.getTransactionsBack().stream().map(TransactionMapper::toDto).collect(Collectors.toList());
        return buildTransaction(transactionLoanOrBorrow)
                .transactionsBack(collect);
    }

    private static TransactionDto.TransactionDtoBuilder buildTransactionBack(TransactionBack transaction) {
        TransactionBack transactionBack = transaction;
        return buildTransaction(transactionBack)
                .transactionIdReference(transactionBack.getTransactionLoanOrBorrow().getId());
    }

    private static TransactionDto.TransactionDtoBuilder buildTransaction(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .name(transaction.getName())
                .description(transaction.getDescription())
                .price(transaction.getPrice())
                .categoryDto(CategoryMapper.toDto(transaction.getCategory()))
                .dateOfPurchase(transaction.getDateOfPurchase());
    }

    private static boolean isTransactionBack(TransactionDto transactionDto, Category category) {
        return transactionDto.getTransactionIdReference() != null && (category.getTransactionType() == TransactionType.LOAN_BACK || category.getTransactionType() == TransactionType.BORROW_BACK);
    }

    private static boolean isLoanOrBorrowTransaction(TransactionDto transactionDto, Category category) {
        return (category.getTransactionType() == TransactionType.LOAN || category.getTransactionType() == TransactionType.BORROW) && transactionDto.getTransactionIdReference() == null;
    }

    private static boolean isSimpleTransaction(TransactionDto transactionDto, Category category) {
        return transactionDto.getTransactionIdReference() == null && (category.getTransactionType() == TransactionType.EXPENSE || category.getTransactionType() == TransactionType.REVENUES);
    }
}
