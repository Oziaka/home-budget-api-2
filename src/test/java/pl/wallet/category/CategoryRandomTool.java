package pl.wallet.category;

import pl.tool.RandomUtils;
import pl.wallet.transaction.TransactionRandomTool;

public class CategoryRandomTool {
    public static Category randomCategory() {
        return Category.builder()
          .name(RandomUtils.randomString())
          .description(RandomUtils.randomString())
          .isDefault(false)
          .type(TransactionRandomTool.randomTransactionType())
          .build();
    }

    public static CategoryDto randomCategoryDto() {
        return CategoryDto.builder()
          .name(RandomUtils.randomString())
          .description(RandomUtils.randomString())
          .isDefault(false)
          .type(TransactionRandomTool.randomTransactionType())
          .build();
    }


}
