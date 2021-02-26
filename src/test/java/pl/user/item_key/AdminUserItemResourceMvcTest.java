package pl.user.item_key;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.user.UserDto;
import pl.user.UserRandomTool;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.Profile.TEST_MVC_PROFILE_NAME;
import static pl.tool.JsonTool.asJsonString;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(TEST_MVC_PROFILE_NAME)
public class AdminUserItemResourceMvcTest {
   @Autowired
   private MockMvc mockMvc;

   @Test
   void addUserItemKeyReturnedUserItemKeyDtoWhenUserIsAdmin() throws Exception {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userDto))
         .contentType(APPLICATION_JSON_VALUE));
      UserItemKeyDto userItemKeyDto = UserItemKeyDto.builder().name("best_friend_name").build();
      // when
      // then
      UserItemKeyDto expectedUserItemKeyDto = UserItemKeyDto.builder().id(2L).name(userItemKeyDto.getName()).build();
      this.mockMvc.perform(put("/admin/user_item_key/add")
         .content(asJsonString(userItemKeyDto))
         .contentType(APPLICATION_JSON_VALUE)
         .with(user(userDto.getEmail()).password(userDto.getPassword()).roles("ADMIN")))
         .andExpect(status().isCreated())
         .andExpect(content().contentType(APPLICATION_JSON_VALUE))
         .andExpect(content().json(asJsonString(expectedUserItemKeyDto)));
   }
}
