package pl.wallet.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.user.UserProvider;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AdminCategoryUnitTest {
   private CategoryRepository categoryRepository;
   private UserProvider userProvider;
   private CategoryService categoryService;
   private AdminCategoryResource adminCategoryResource;

   @BeforeEach
   void init() {
      categoryRepository = Mockito.mock(CategoryRepository.class);
      userProvider = Mockito.mock(UserProvider.class);
      categoryService = new CategoryService(categoryRepository, userProvider);
      adminCategoryResource = new AdminCategoryResource(categoryService);
   }

   @Test
   void getDefaultCategoriesReturnDefaultCategories() {
      // given
      Set<Category> defaultCategories = Stream.iterate(0, i -> i + 1).limit(10L).map(i -> CategoryRandomTool.randomCategory()).collect(Collectors.toSet());
      // when
      when(categoryRepository.findAllByIsDefaultIsTrue()).thenReturn(defaultCategories);
      ResponseEntity<Set<CategoryDto>> defautlCategoriesResponseEntity = adminCategoryResource.getDefaultCategories();
      // then
      Set<CategoryDto> expectedDefaultCateogiresDto = defaultCategories.stream().map(CategoryMapper::toDto).collect(Collectors.toSet());
      assertEquals(expectedDefaultCateogiresDto, defautlCategoriesResponseEntity.getBody());
   }

   @Test
   void editDefaultCategoryReturnDefaultCategoryWhenEditedFinishSuccess() {
      // given
      Category category = CategoryRandomTool.randomCategory();
      category.setIsDefault(true);
      category.setId(1L);
      CategoryDto updatedCategoryDto = CategoryMapper.toDto(CategoryRandomTool.randomCategory());
      // when
      when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
      when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Category.class));
      ResponseEntity<CategoryDto> updatedCategoryDtoResponseEntity = adminCategoryResource.editDefaultCategory(category.getId(), updatedCategoryDto);
      // then
      CategoryDto expectedUpdatedCategoryDto = CategoryDto.builder().name(updatedCategoryDto.getName()).isDefault(false).description(updatedCategoryDto.getDescription()).type(category.getType()).id(category.getId()).build();
      assertEquals(HttpStatus.OK, updatedCategoryDtoResponseEntity.getStatusCode());
      assertEquals(expectedUpdatedCategoryDto, updatedCategoryDtoResponseEntity.getBody());
   }
}
