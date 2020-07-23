package pl.wallet.category;

public class CategoryMapper {
    private CategoryMapper() {
    }

    public static Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setType(categoryDto.getType());
        return category;
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .description(category.getDescription())
                .isDefault(category.getIsDefault())
                .build();
    }
}
