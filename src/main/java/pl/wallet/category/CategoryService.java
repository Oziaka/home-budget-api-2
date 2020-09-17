package pl.wallet.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.DataNotFoundExeption;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserProvider;

import java.security.Principal;
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
      user.setCategories(defaultCategories);
      this.userProvider.save(user);
      return getCategories(principal);
   }

   Set<CategoryDto> getDefaultCategories() {
      return this.getAllDefaults().stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
   }

   CategoryDto editDefaultCategory(Long categoryId, CategoryDto categoryDto) {
      Category category = this.get(categoryId);
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
      Category category = this.get(user, categoryId);
      if (category.getIsDefault())
         throw new RuntimeException("You can not edit default or not exist category");
      category = updateNotNullFieldsInCategoryDtoToCategory(category, categoryDto);
      Category savedCategory = this.save(category);
      return CategoryMapper.toDto(savedCategory);
   }

   public Category get(User user, Long categoryId) {
      return categoryRepository.findByIdAndUsers(categoryId, user).orElseThrow(ThereIsNoYourPropertyException::new);
   }

   private Category get(Long categoryId) {
      return categoryRepository.findById(categoryId).orElseThrow(() ->
         new DataNotFoundExeption("Category not found"));
   }

   private Category save(Category category) {
      return categoryRepository.save(category);
   }

   private Set<Category> getAll(User user) {
      return categoryRepository.findByUsers(user);
   }

   public Set<Category> getAllDefaults() {
      return categoryRepository.getDefaultCategories();
   }

   public Category get(String email, Long categoryId) {
      return categoryRepository.get(email, categoryId).orElseThrow(ThereIsNoYourPropertyException::new);
   }
}
