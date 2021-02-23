package pl.user.item_key;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode
public class UserItemKeyDto {

  private Long id;
  @NotNull
  private String name;

  @Builder
  public UserItemKeyDto(Long id, @NotNull String name) {
    this.id = id;
    this.name = name;
  }
}
