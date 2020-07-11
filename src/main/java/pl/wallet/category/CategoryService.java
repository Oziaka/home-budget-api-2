package pl.wallet.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.EntityNotFoundException;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;

import java.util.Set;

@Service
@AllArgsConstructor
public class CategoryService {

  private CategoryRepository categoryRepository;

  public Category getCategory (User user, Long categoryId) {
    return categoryRepository.findByIdAndUsers(categoryId, user).orElseThrow(ThereIsNoYourPropertyException::new);
  }

  public Category getCategory (Long categoryId) {
    return categoryRepository.findById(categoryId).orElseThrow(() ->
      new EntityNotFoundException(categoryId, categoryId.getClass()));
  }

//  public Category getCategory (Long categoryId) {
//    return categoryRepository.getById(categoryId).orElseThrow(() -> new EntityNotFoundException(categoryId, categoryId.getClass()));
//  }

  Category save (Category category) {
    return categoryRepository.save(category);
  }

  Set<Category> getCategoriesByUser (User user) {
    return categoryRepository.findByUsers(user);
  }

  public Set<Category> getDefaultCategories () {
    return categoryRepository.getDefaultCategories();
  }
}
