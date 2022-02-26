package pl.wallet.transaction.mapper;

import pl.wallet.category.Category;
import pl.wallet.transaction.dto.TransactionRecurringDto;
import pl.wallet.transaction.model.TransactionRecurring;

public class TransactionRecurringMapper {
    public static TransactionRecurringDto toDto(TransactionRecurring transactionRecurring) {
        return null;
    }

    public static TransactionRecurring toEntity(TransactionRecurringDto transactionRecurringDto, Category category) {
        return TransactionRecurring.builder()
          .start(transactionRecurringDto.getStart())
          .frequency(transactionRecurringDto.getFrequency())
          .numberOfRepetition(transactionRecurringDto.getNumberOfRepetition())
          .transaction(TransactionMapper.toEntity(transactionRecurringDto.getTransaction(), category))
          .build();
    }
}
