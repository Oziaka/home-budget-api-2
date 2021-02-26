package pl.wallet.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
   Optional<Category> getById(Long categoryId);

   @Query("SELECT c FROM Category c JOIN c.users u WHERE u = :user")
   Set<Category> findByUsers(@Param("user") User user);

   @Query("SELECT c FROM Category c WHERE c.isDefault = true")
   Set<Category> getDefaultCategories();

   @Query("SELECT c FROM Category c INNER JOIN c.users u WHERE :categoryId = c.id AND u = :user")
   Optional<Category> findByIdAndUsers(@Param("categoryId") Long categoryId, @Param("user") User user);

   @Query("SELECT c FROM User u INNER JOIN u.categories c WHERE u.email = :email AND c.id = :categoryId")
   Optional<Category> find(@Param("email") String email, @Param("categoryId") Long categoryId);
}
