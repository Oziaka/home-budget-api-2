package pl.security.user_role;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class UserRoleDto {

  private Long id;

  private String name;

  private String description;

  @Builder
  public UserRoleDto (Long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
