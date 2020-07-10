package pl.wallet.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.wallet.transaction.TransactionType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto {

  @Null
  private Long id;

  @NotEmpty(message = "Category must have name")
  private String name;

  private String description;

  @NotNull(message = "Category must have transaction type")
  private TransactionType transactionType;

  private Boolean isDefault;

  @Builder
  public CategoryDto (Long id, @NotEmpty(message = "Category must have name") String name, String description, @NotNull(message = "Category must have transaction type") TransactionType transactionType, Boolean isDefault) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.transactionType = transactionType;
    this.isDefault = isDefault;
  }
}
