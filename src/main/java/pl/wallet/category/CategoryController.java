package pl.wallet.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.exception.CanNotEditCategoryExcpetion;
import pl.user.User;
import pl.user.UserService;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;
    private UserService userService;

    CategoryDto addCategory(Principal principal, CategoryDto categoryDto) {
        User user = userService.getUser(principal);
        Category category = CategoryMapper.toEntity(categoryDto);
        user.addCategory(category);
        category.addUser(user);
        category = categoryService.save(category);
        return CategoryMapper.toDto(category);
    }

    void removeCategory(Principal principal, Long categoryId) {
        User user = userService.getUser(principal);
        user.removeCategory(categoryId);
        userService.save(user);
    }

    Set<CategoryDto> getCategories(Principal principal) {
        User user = userService.getUser(principal);
        return categoryService.getCategoriesByUser(user).stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
    }

    Set<CategoryDto> restoreDefaultCategories(Principal principal) {
        User user = userService.getUser(principal);
        Set<Category> defaultCategories = categoryService.getDefaultCategories();
        user.setCategories(defaultCategories);
        this.userService.save(user);
        return getCategories(principal);
    }

    Set<CategoryDto> getDefaultCategories() {
        return categoryService.getDefaultCategories().stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
    }

    CategoryDto editDefaultCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryService.getCategory(categoryId);
        if (!category.getIsDefault())
            throw new CanNotEditCategoryExcpetion("You can not edit no default or not exist category");
        category = updateNotNullFieldsInCategoryDtoToCategory(category, categoryDto);
        if (categoryDto.getIsDefault() != null) category.setIsDefault(categoryDto.getIsDefault());
        Category savedCategory = categoryService.save(category);
        return CategoryMapper.toDto(savedCategory);
    }

    private Category updateNotNullFieldsInCategoryDtoToCategory(Category category, CategoryDto categoryDto) {
        Category category2 = category;
        if (categoryDto.getName() != null) category2.setName(categoryDto.getName());
        if (categoryDto.getDescription() != null) category2.setDescription(categoryDto.getDescription());
        return category2;
    }

    CategoryDto editCategory(Principal principal, Long categoryId, CategoryDto categoryDto) {
        User user = userService.getUser(principal);
        Category category = categoryService.getCategory(user, categoryId);
        if (category.getIsDefault())
            throw new CanNotEditCategoryExcpetion("You can not edit default or not exist category");
        category = updateNotNullFieldsInCategoryDtoToCategory(category, categoryDto);
        Category savedCategory = categoryService.save(category);
        return CategoryMapper.toDto(savedCategory);
    }
}
