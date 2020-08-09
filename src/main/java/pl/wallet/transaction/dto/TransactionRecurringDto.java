package pl.wallet.transaction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.wallet.transaction.enums.Frequency;
import pl.wallet.transaction.model.Transaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
public class TransactionRecurringDto {

   @Null
   private Long id;

   private TransactionDto transaction;

   @NotNull
   private Frequency frequency;

   @NotNull
   private LocalDateTime start;

   private LocalDateTime end;

   @Null
   private LocalDateTime dateOfLastAdding;

   @Null
   private LocalDateTime dateOfNextAdding;

   private Long numberOfRepetition;

   @Null
   private Long currentNumberRepetitions;

   public TransactionRecurringDto(@Null Long id, TransactionDto transaction, @NotNull Frequency frequency, @NotNull LocalDateTime start, LocalDateTime end, @Null LocalDateTime dateOfLastAdding, @Null LocalDateTime dateOfNextAdding, Long numberOfRepetition, @Null Long currentNumberRepetitions) {
      this.id = id;
      this.transaction = transaction;
      this.frequency = frequency;
      this.start = start;
      this.end = end;
      this.dateOfLastAdding = dateOfLastAdding;
      this.dateOfNextAdding = dateOfNextAdding;
      this.numberOfRepetition = numberOfRepetition;
      this.currentNumberRepetitions = currentNumberRepetitions;
   }
}
