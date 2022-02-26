package pl.wallet.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.user.UserDto;
import pl.user.UserRandomTool;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static pl.Profile.TEST_MVC_PROFILE_NAME;
import static pl.tool.JsonTool.asJsonString;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(TEST_MVC_PROFILE_NAME)
public class AdminCategoryResourceMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getDefaultCategoriesReturneSetOfDefaultCategories() throws Exception {
        //given
        UserDto userDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(put("/register")
          .content(asJsonString(userDto))
          .contentType(APPLICATION_JSON_VALUE));
        //when
        //then
        this.mockMvc.perform(get("/admin/category/default_categories")
            .with(user(userDto.getEmail()).password(userDto.getPassword()).roles("ADMIN")))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andDo(print());
    }
}
