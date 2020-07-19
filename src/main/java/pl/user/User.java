package pl.user;

import lombok.*;
import pl.security.user_role.UserRole;
import pl.user.user_notification.notification.UserNotification;
import pl.wallet.Wallet;
import pl.wallet.category.Category;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;
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

    private String userName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_user_role"))
    private Set<UserRole> roles;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "user")
    private List<Wallet> wallets;

    @ManyToMany
    private Set<Category> categories;

    @ElementCollection
    @CollectionTable(name = "user_item",
            joinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "user_id")})
    @MapKeyColumn(name = "\"key\"")
    @Column(name = "value")
    private Map<String, String> items;

    @OneToMany(mappedBy = "user")
    private List<UserNotification> userNotifications;

    @Builder
    public User(List<UserNotification> userNotifications, @Email String email, String password, String userName, Set<UserRole> roles, List<Wallet> wallets, Set<Category> categories, Map<String, String> items) {
        this.userNotifications = userNotifications;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.roles = roles;
        this.wallets = wallets;
        this.categories = categories;
        this.items = items;
    }

    public void addItem(String key, String value) {
        if (items == null)
            items = new HashMap<>();
        items.compute(key, (k, l) -> value);
    }

    public void addCategory(Category category) {
        if (categories == null)
            categories = new HashSet<>();
        categories.add(category);
    }

    public void addRole(UserRole userRole) {
        if (roles == null)
            roles = new HashSet<>();
        roles.add(userRole);
    }

    public void addWallet(Wallet wallet) {
        if (wallets == null)
            wallets = new ArrayList<>();
        wallets.add(wallet);
    }

    public void removeCategory(Long categoryId) {
        this.setCategories(this.categories.stream().filter(c -> !c.getId().equals(categoryId)).collect(Collectors.toSet()));
    }

    public void removeRole(UserRole userRole) {
        this.roles.remove(userRole);
    }
}
