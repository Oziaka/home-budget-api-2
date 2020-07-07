package pl.wallet.transaction;

import pl.exception.InvalidTransactionException;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryMapper;

import java.util.stream.Collectors;

class TransactionMapper {

  static Transaction toEntity (TransactionDto transactionDto, Category category) {
    if(isTransactionBack(transactionDto, category))
      return TransactionBack.transactionBackBuilder()
        .name(transactionDto.getName())
        .description(transactionDto.getDescription())
        .price(transactionDto.getPrice())
        .dateOfPurchase(transactionDto.getDateOfPurchase())
        .category(category)
        .build();
    else if((isLoanOrBorrowTransaction(transactionDto, category)))
      return TransactionLoanOrBorrow.transactionLoanOrBorrowBuilder()
        .name(transactionDto.getName())
        .description(transactionDto.getDescription())
        .price(transactionDto.getPrice())
        .dateOfPurchase(transactionDto.getDateOfPurchase())
        .category(category)
        .build();
    else if(isSimpleTransaction(transactionDto, category))
      return Transaction.builder()
        .name(transactionDto.getName())
        .description(transactionDto.getDescription())
        .price(transactionDto.getPrice())
        .dateOfPurchase(transactionDto.getDateOfPurchase())
        .category(category)
        .build();
    else throw new InvalidTransactionException();
  }

  static TransactionDto toDto (Transaction transaction) {
    if(transaction instanceof TransactionLoanOrBorrow) {
      TransactionLoanOrBorrow transactionLoanOrBorrow = (TransactionLoanOrBorrow) transaction;
      return TransactionDto.builder()
        .id(transactionLoanOrBorrow.getId())
        .name(transactionLoanOrBorrow.getName())
        .description(transactionLoanOrBorrow.getDescription())
        .price(transactionLoanOrBorrow.getPrice())
        .categoryDto(CategoryMapper.toDto(transactionLoanOrBorrow.getCategory()))
        .dateOfPurchase(transactionLoanOrBorrow.getDateOfPurchase())
        .transactionsBack(transactionLoanOrBorrow.getTransactionsBack().stream().map(TransactionMapper::toDto).collect(Collectors.toList()))
        .build();
    }
    return TransactionDto.builder()
      .id(transaction.getId())
      .name(transaction.getName())
      .description(transaction.getDescription())
      .price(transaction.getPrice())
      .categoryDto(CategoryMapper.toDto(transaction.getCategory()))
      .dateOfPurchase(transaction.getDateOfPurchase())
      .build();
  }

  private static boolean isTransactionBack (TransactionDto transactionDto, Category category) {
    return transactionDto.getTransactionIdReference() != null && (category.getTransactionType() == TransactionType.LOAN_BACK || category.getTransactionType() == TransactionType.BORROW_BACK);
  }

  private static boolean isLoanOrBorrowTransaction (TransactionDto transactionDto, Category category) {
    return category.getTransactionType() == TransactionType.LOAN || category.getTransactionType() == TransactionType.BORROW;
  }

  private static boolean isSimpleTransaction (TransactionDto transactionDto, Category category) {
    return transactionDto.getTransactionIdReference() == null && (category.getTransactionType() == TransactionType.EXPENSE || category.getTransactionType() == TransactionType.REVENUES);
  }
}
