package pl.wallet.category;

import org.apache.commons.lang3.RandomUtils;
import pl.wallet.transaction.dto.TransactionDto;
import pl.wallet.transaction.enums.Type;
import pl.wallet.transaction.model.TransactionLoanOrBorrow;

import java.time.LocalDateTime;

public class TransactionRandomTool {

  public static Type randomTransactionType() {
    return Type.values()[RandomUtils.nextInt(0, Type.values().length)];
  }

  public static TransactionDto randomTransactionDto() {
    return TransactionDto.builder()
      .name(pl.tool.RandomUtils.randomString())
      .dateOfPurchase(LocalDateTime.now())
      .description(pl.tool.RandomUtils.randomString())
      .price(pl.tool.RandomUtils.randomBigDecimal())
      .categoryId(pl.tool.RandomUtils.randomLong())
      .build();
  }

  public static TransactionLoanOrBorrow randomTransactionLoanOrBorrw() {
    return TransactionLoanOrBorrow.transactionLoanOrBorrowBuilder()
      .name(pl.tool.RandomUtils.randomString())
      .dateOfPurchase(LocalDateTime.now())
      .description(pl.tool.RandomUtils.randomString())
      .price(pl.tool.RandomUtils.randomBigDecimal())
      .build();
  }

  public static TransactionDto randomTransactionBackDto(Long transactionIdReference) {
    return TransactionDto.builder()
      .name(pl.tool.RandomUtils.randomString())
      .dateOfPurchase(LocalDateTime.now())
      .description(pl.tool.RandomUtils.randomString())
      .price(pl.tool.RandomUtils.randomBigDecimal())
      .transactionIdReference(transactionIdReference)
      .build();
  }
}
