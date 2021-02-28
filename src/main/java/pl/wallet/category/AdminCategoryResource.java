package pl.wallet.category;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Validated
@RestController
@CrossOrigin("${cors.allowed-origins}")
@RequestMapping(path = "/admin/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminCategoryResource {

   private CategoryService categoryService;

   @GetMapping(path = "/default_categories", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Set<CategoryDto>> getDefaultCategories() {
      return ResponseEntity.ok(categoryService.getDefaultCategories());
   }

   @PostMapping(path = "/edit/{categoryId}")
   public ResponseEntity<CategoryDto> editDefaultCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
      return ResponseEntity.ok(categoryService.editDefaultCategory(categoryId, categoryDto));
   }

}
