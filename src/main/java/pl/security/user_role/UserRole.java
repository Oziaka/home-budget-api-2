package pl.security.user_role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Setter
@Table(name = "user_role")
public class UserRole {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "user_role_id")
   private Long id;

   @Column(unique = true)
   private String roleName;

   private String description;

   private Boolean isDefault;

   public UserRole(String roleName, String description) {
      this.roleName = roleName;
      this.description = description;
   }

}

