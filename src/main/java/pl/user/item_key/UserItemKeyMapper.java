package pl.user.item_key;


public class UserItemKeyMapper {

  public static UserItemKeyDto toDto (UserItemKey userItemKey) {
    return UserItemKeyDto.builder().id(userItemKey.getId()).name(userItemKey.getName()).build();
  }

  public static UserItemKey toEntity (UserItemKeyDto userItemKeyDto) {
    return UserItemKey.builder().name(userItemKeyDto.getName()).build();
  }
}
