package pl.wallet.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class CategoryProvider {

    private final CategoryRepository categoryRepository;
    public Set<Category> getAllDefaults() {
        return categoryRepository.findAllByIsDefaultIsTrue();
    }


}
