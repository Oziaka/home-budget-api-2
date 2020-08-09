package pl.user.item_key;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
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
