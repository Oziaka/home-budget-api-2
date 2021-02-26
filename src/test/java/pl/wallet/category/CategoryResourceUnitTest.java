package pl.wallet.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.user.*;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CategoryResourceUnitTest {
   private CategoryRepository categoryRepository;
   private UserProvider userProvider;
   private CategoryService categoryService;
   private CategoryResource categoryResource;

   @BeforeEach
   void init() {
      categoryRepository = Mockito.mock(CategoryRepository.class);
      userProvider = Mockito.mock(UserProvider.class);
      categoryService = new CategoryService(categoryRepository, userProvider);
      categoryResource = new CategoryResource(categoryService);
   }

   @Test
   void addCategoryReturnCategoryDtoWhenCategoryIsValidAndUserIsAuth() {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      Category category = CategoryRandomTool.randomCategory();
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
      when(categoryRepository.save(any())).thenAnswer(invocation -> {
         Category argument = invocation.getArgument(0, Category.class);
         argument.setId(1L);
         return argument;
      });
      ResponseEntity<CategoryDto> categoryDtoResponseEntity = categoryResource.addCategory(userDto::getEmail, CategoryMapper.toDto(category));
      // then
      category.addUser(UserMapper.toEntity(userDto));
      category.setId(1L);
      CategoryDto expectedCategoryDto = CategoryMapper.toDto(category);
      assertEquals(HttpStatus.CREATED, categoryDtoResponseEntity.getStatusCode());
      assertEquals(expectedCategoryDto, categoryDtoResponseEntity.getBody());
   }

   @Test
   void removeCategoryReturnHttpStatus() {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      Category category = CategoryRandomTool.randomCategory();
      Long cateogryId = 1L;
      category.setId(cateogryId);
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).categories(Set.of(category)).build());
      when(userProvider.save(any())).thenAnswer(u -> u.getArgument(0, User.class));
      ResponseEntity removeCategoryResponseEntity = categoryResource.removeCategory(userDto::getEmail, cateogryId);
      // then
      assertEquals(HttpStatus.NO_CONTENT, removeCategoryResponseEntity.getStatusCode());
      Assertions.assertNull(removeCategoryResponseEntity.getBody());
   }

   @Test
   void getUserCategoriesReturnUserCategories() {
      // given
      User user = UserRandomTool.randomUser();
      Set<Category> userCategories = Stream.iterate(0, i -> i + 1).limit(10).map(i -> CategoryRandomTool.randomCategory()).collect(Collectors.toSet());
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
      when(categoryRepository.findByUsers(any())).thenReturn(userCategories);
      ResponseEntity<Set<CategoryDto>> userCategoriesResponseEntity = categoryResource.getUserCategories(user::getEmail);
      // then
      Set<CategoryDto> expectedUserCategories = userCategories.stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
      assertEquals(HttpStatus.OK, userCategoriesResponseEntity.getStatusCode());
      assertEquals(expectedUserCategories, userCategoriesResponseEntity.getBody());
   }

   @Test
   void restoreDefaultCategoriesReturnUserCategories() {
      // given
      User user = UserRandomTool.randomUser();
      Set<Category> userCategories = Stream.iterate(0, i -> i + 1).limit(10).map(i -> CategoryRandomTool.randomCategory()).collect(Collectors.toSet());
      Set<Category> defaultCategories = Stream.iterate(0, i -> i + 1).limit(10).map(i -> CategoryRandomTool.randomCategory()).collect(Collectors.toSet());
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).categories(userCategories).build());
      when(categoryRepository.getDefaultCategories()).thenReturn(defaultCategories);
      ResponseEntity<Set<CategoryDto>> userCategoriesResponseEntity = categoryResource.restoreDefaultCategories(user::getEmail);
      // then
      Set<CategoryDto> expectedUserCategories = userCategories.stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
      expectedUserCategories.addAll(defaultCategories.stream().map(CategoryMapper::toDto).collect(Collectors.toSet()));
      assertEquals(HttpStatus.OK, userCategoriesResponseEntity.getStatusCode());
      assertEquals(expectedUserCategories, userCategoriesResponseEntity.getBody());
   }

   @Test
   void editCategoryReturnUpdatedCategoryWhenUserUpdateOwnCategoryAndCategoryIsNotDefaultCategory() {
      // given
      User user = UserRandomTool.randomUser();
      Category category = CategoryRandomTool.randomCategory();
      category.setId(1L);
      CategoryDto updatedCategoryDto = CategoryMapper.toDto(CategoryRandomTool.randomCategory());
      // when
      when(categoryRepository.findByIdAndUsers(any(), any())).thenReturn(Optional.of(category));
      when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Category.class));
      ResponseEntity<CategoryDto> updatedCategoryDtoResponseEntity = categoryResource.editCategory(user::getEmail, category.getId(), updatedCategoryDto);
      // then
      CategoryDto expectedUpdatedCategoryDto = CategoryDto.builder().name(updatedCategoryDto.getName()).isDefault(false).description(updatedCategoryDto.getDescription()).type(category.getType()).id(category.getId()).build();
      assertEquals(HttpStatus.OK, updatedCategoryDtoResponseEntity.getStatusCode());
      assertEquals(expectedUpdatedCategoryDto, updatedCategoryDtoResponseEntity.getBody());
   }


}
