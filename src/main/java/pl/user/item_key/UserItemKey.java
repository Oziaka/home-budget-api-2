package pl.user.item_key;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserItemKey {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_item_key_id")
  private Long id;

  private String name;

  @Builder
  public UserItemKey(String name) {
    this.name = name;
  }
}
