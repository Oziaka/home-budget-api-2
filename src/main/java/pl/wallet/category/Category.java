package pl.wallet.category;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.user.User;
import pl.wallet.transaction.enums.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Data
@Entity
@Table(name = "category")
public class Category {

    private Boolean isDefault = false;

    @Column(name = "category_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated
    private Type type;

    @ManyToMany(mappedBy = "categories")
    private List<User> users;

    @Builder
    public Category(Boolean isDefault, String name, String description, Type type, List<User> users) {
        this.isDefault = isDefault;
        this.name = name;
        this.description = description;
        this.type = type;
        this.users = users;
    }


    public void addUser(User user) {
        if (users == null) {
            this.users = new ArrayList<>();
            users.add(user);
        } else
            users.add(user);
    }


    public List<User> getUser() {
        return this.users;
    }


    public void setUserList(List<User> users) {
        this.users = users;
    }
}
