package pl.user;

import lombok.*;
import pl.security.user_role.UserRole;
import pl.wallet.Wallet;
import pl.wallet.category.Category;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"user\"")
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @ManyToMany(fetch = FetchType.EAGER)
  private Set<UserRole> roles;

  @OneToMany(cascade = CascadeType.MERGE, mappedBy = "user")
  private List<Wallet> wallets;

  @ManyToMany
  private Set<Category> categories;

  private Long favoriteWalletId;

  public void addCategory (Category category) {
    try {
      categories.add(category);
    } catch (NullPointerException e) {
      categories = new HashSet<>();
      categories.add(category);
    }
  }


  public void addRole (UserRole userRole) {
    try {
      this.roles.add(userRole);
    } catch (NullPointerException e) {
      this.roles = new HashSet<>();
      this.roles.add(userRole);
    }
  }

  public void addWallet (Wallet wallet) {
    try {
      this.wallets.add(wallet);
    } catch (NullPointerException e) {
      this.wallets = new ArrayList<>();
      this.wallets.add(wallet);
    }
  }

  public void removeCategory (Long categoryId) {
    this.setCategories(this.categories.stream().filter(c -> !c.getId().equals(categoryId)).collect(Collectors.toSet()));
  }

  public void removeRole (UserRole userRole) {
    this.roles.remove(userRole);
  }
}
