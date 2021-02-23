package pl.wallet.category;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@Validated
@RestController
@CrossOrigin("${cors.allowed-origins}")
@RequestMapping(path = "/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CategoryResource {

  private CategoryService categoryService;

  @PutMapping(path = "/add")
  public ResponseEntity<CategoryDto> addCategory(Principal principal, @Valid @RequestBody CategoryDto categoryDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(principal, categoryDto));
  }

  @DeleteMapping(path = "/remove/{categoryId}", produces = MediaType.ALL_VALUE, consumes = MediaType.ALL_VALUE)
  public ResponseEntity removeCategory(Principal principal, @PathVariable Long categoryId) {
    categoryService.removeCategory(principal, categoryId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(consumes = MediaType.ALL_VALUE)
  public ResponseEntity<Set<CategoryDto>> getUserCategories(Principal principal) {
    return ResponseEntity.ok(categoryService.getCategories(principal));
  }

  @PostMapping(path = "/restore_default_categories", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<Set<CategoryDto>> restoreDefaultCategories(Principal principal) {
    return ResponseEntity.ok(categoryService.restoreDefaultCategories(principal));
  }

  @PostMapping(path = "/edit/{categoryId}")
  public ResponseEntity<CategoryDto> editCategory(Principal principal, @PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
    return ResponseEntity.ok(categoryService.editCategory(principal, categoryId, categoryDto));
  }

  @GetMapping(path = "/admin/default_categories")
  public ResponseEntity<Set<CategoryDto>> getDefaultCategories() {
    return ResponseEntity.ok(categoryService.getDefaultCategories());
  }

  @PostMapping(path = "/admin/edit/{categoryId}")
  public ResponseEntity<CategoryDto> editDefaultCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
    return ResponseEntity.ok(categoryService.editDefaultCategory(categoryId, categoryDto));
  }


}

