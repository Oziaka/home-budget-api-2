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

    public Category get(User user, Long categoryId) {
        return categoryRepository.findByIdAndUsers(categoryId, user).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    Category get(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new EntityNotFoundException(categoryId, categoryId.getClass()));
    }

    Category save(Category category) {
        return categoryRepository.save(category);
    }

    Set<Category> getAll(User user) {
        return categoryRepository.findByUsers(user);
    }

    public Set<Category> getAllDefaults() {
        return categoryRepository.getDefaultCategories();
    }

    public Category get(String email, Long categoryId) {
        return categoryRepository.get(email, categoryId).orElseThrow(ThereIsNoYourPropertyException::new);
    }
}
