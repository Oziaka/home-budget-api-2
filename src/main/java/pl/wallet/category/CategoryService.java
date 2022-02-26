package pl.wallet.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.DataNotFoundException;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserProvider;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;
    private UserProvider userProvider;

    CategoryDto addCategory(Principal principal, CategoryDto categoryDto) {
        User user = userProvider.get(principal);
        Category category = CategoryMapper.toEntity(categoryDto);
        user.addCategory(category);
        category.addUser(user);
        category = this.save(category);
        return CategoryMapper.toDto(category);
    }

    void removeCategory(Principal principal, Long categoryId) {
        User user = userProvider.get(principal);
        user.removeCategory(categoryId);
        userProvider.save(user);
    }

    Set<CategoryDto> getCategories(Principal principal) {
        User user = userProvider.get(principal);
        return this.getAll(user).stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
    }

    Set<CategoryDto> restoreDefaultCategories(Principal principal) {
        User user = userProvider.get(principal);
        Set<Category> defaultCategories = this.getAllDefaults();
        defaultCategories.forEach(user::addCategory);
        userProvider.save(user);
        return user.getCategories().stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
    }

    Set<CategoryDto> getDefaultCategories() {
        return this.getAllDefaults().stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
    }

    CategoryDto editDefaultCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = this.getCategory(categoryId);
        if (!category.getIsDefault())
            throw new RuntimeException("You can not edit no default or not exist category");
        updateNotNullFieldsInCategoryDtoToCategory(category, categoryDto);
        if (categoryDto.getIsDefault() != null) category.setIsDefault(categoryDto.getIsDefault());
        Category savedCategory = this.save(category);
        return CategoryMapper.toDto(savedCategory);
    }

    private Category updateNotNullFieldsInCategoryDtoToCategory(Category category, CategoryDto categoryDto) {
        if (categoryDto.getName() != null) category.setName(categoryDto.getName());
        if (categoryDto.getDescription() != null) category.setDescription(categoryDto.getDescription());
        return category;
    }

    CategoryDto editCategory(Principal principal, Long categoryId, CategoryDto categoryDto) {
        User user = userProvider.get(principal);
        Category category = this.getCategory(user, categoryId);
        if (category.getIsDefault())
            throw new RuntimeException("You can not edit default or not exist category");
        category = updateNotNullFieldsInCategoryDtoToCategory(category, categoryDto);
        Category savedCategory = this.save(category);
        return CategoryMapper.toDto(savedCategory);
    }

    public Category getCategory(User user, Long categoryId) {
        return categoryRepository.findByIdAndUsers(categoryId, user).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
          new DataNotFoundException("Category not found"));
    }

    private Category save(Category category) {
        return categoryRepository.save(category);
    }

    private Set<Category> getAll(User user) {
        return categoryRepository.findByUsers(user);
    }

    public Set<Category> getAllDefaults() {
        return categoryRepository.findAllByIsDefaultIsTrue();
    }

    public Optional<Category> getCategory(String email, Long categoryId) {
        return categoryRepository.find(email, categoryId);
    }
}
