package pl.wallet.transaction.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import pl.wallet.category.CategoryDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class TransactionDto {

  private Long id;

  @Length(min = 3, message = "Transaction should have name")
  @NotNull(message = "Transaction must have name")
  private String name;

  private String description;

  @NotNull(message = "Transaction must have price")
  private BigDecimal price;

  private LocalDateTime dateOfPurchase;

  private Long categoryId;

  @Null
  private CategoryDto categoryDto;

  @Null
  private List<TransactionDto> transactionsBack;

  private Long transactionIdReference;

  @Builder
  public TransactionDto(Long id, @Length(min = 3, message = "Transaction should have name") @NotNull(message = "Transaction must have name") String name, String description, @NotNull(message = "Transaction must have price") BigDecimal price, LocalDateTime dateOfPurchase, Long categoryId, @Null CategoryDto categoryDto, @Null List<TransactionDto> transactionsBack, Long transactionIdReference) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.dateOfPurchase = dateOfPurchase;
    this.categoryId = categoryId;
    this.categoryDto = categoryDto;
    this.transactionsBack = transactionsBack;
    this.transactionIdReference = transactionIdReference;
  }

}
