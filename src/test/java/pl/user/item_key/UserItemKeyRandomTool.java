package pl.user.item_key;

import pl.tool.RandomUtils;

public class UserItemKeyRandomTool {
   public static UserItemKey randomUserItemKey() {
      return UserItemKey.builder().name(RandomUtils.randomString()).build();
   }
}
