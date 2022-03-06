package pl.wallet.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.user.UserDto;
import pl.user.UserRandomTool;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Profile.TEST_MVC_PROFILE_NAME;
import static pl.tool.JsonTool.asJsonString;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(TEST_MVC_PROFILE_NAME)
public class CategoryResourceMvcTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    void addCategoryReturnedCategoryDtoWhenCategoryAdded() throws Exception {
        UserDto userDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(post("/api/register")
          .content(asJsonString(userDto))
          .contentType(APPLICATION_JSON_VALUE));
        CategoryDto categoryDto = CategoryRandomTool.randomCategoryDto();
        CategoryDto expectedCategoryDto = categoryDto;
        this.mockMvc.perform(post("/api/category/add").with(user(userDto.getEmail()).password(userDto.getPassword()))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(expectedCategoryDto)))
          .andExpect(content().contentType(APPLICATION_JSON_VALUE))
          .andExpect(status().isCreated());
    }

    @Test
    void getUserCategoriesReturnedAllUserCategories() throws Exception {
        UserDto userDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(post("/api/register")
          .content(asJsonString(userDto))
          .contentType(MediaType.APPLICATION_JSON_VALUE));
        this.mockMvc.perform(get("/api/category")
            .with(user(userDto.getEmail()).password(userDto.getPassword())))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$", hasSize(8)));
    }

    @Test
    void removeCategoryDoNotReturnedAnything() throws Exception {
        UserDto userDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(post("/api/register")
          .content(asJsonString(userDto))
          .contentType(APPLICATION_JSON_VALUE));
        CategoryDto categoryDto = CategoryRandomTool.randomCategoryDto();
        this.mockMvc.perform(post("/api/category/add").with(user(userDto.getEmail()).password(userDto.getPassword()))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(asJsonString(categoryDto)));
        this.mockMvc.perform(delete("/api/category/remove/2").with(user(userDto.getEmail()).password(userDto.getPassword()))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJsonString(categoryDto)))
          .andExpect(status().isNoContent())
          .andExpect(content().string(""));
    }

    @Test
    void restoreDefaultCategoriesReturnedAllUserCategories() throws Exception {
        UserDto userDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(post("/api/register")
          .content(asJsonString(userDto))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(post("/api/category/restore_default_categories").with(user(userDto.getEmail()).password(userDto.getPassword())))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(8)));
    }

    @Test
    void editCategoryReturnedUpdatedCategory() throws Exception {
        UserDto userDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(post("/api/register")
          .content(asJsonString(userDto))
          .contentType(APPLICATION_JSON_VALUE));
        CategoryDto categoryDto = CategoryRandomTool.randomCategoryDto();
        this.mockMvc.perform(post("/api/category/add")
          .with(user(userDto.getEmail()).password(userDto.getPassword()))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(asJsonString(categoryDto)));
        CategoryDto updatedCategoryDto = CategoryRandomTool.randomCategoryDto();
        CategoryDto expectedCategoryDto = CategoryDto.builder().id(9L).isDefault(false).description(updatedCategoryDto.getDescription()).name(updatedCategoryDto.getName()).type(categoryDto.getType()).build();
        this.mockMvc.perform(post("/api/category/edit/9")
            .contentType(APPLICATION_JSON_VALUE)
            .with(user(userDto.getEmail()).password(userDto.getPassword()))
            .content(asJsonString(updatedCategoryDto)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON_VALUE))
          .andExpect(content().json(asJsonString(expectedCategoryDto)));

    }


}
