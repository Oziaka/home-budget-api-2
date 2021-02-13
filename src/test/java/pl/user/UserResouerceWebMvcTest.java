package pl.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tool.JsonTool.asJsonString;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserResouerceWebMvcTest {

   @Autowired
   private MockMvc mockMvc;

   @Test
   void registerReturnUserDtoWhenRegisteredSuccessful() throws Exception {
      UserDto userToRegistration = UserRandomTool.randomUserDto();
      UserDto expectedUserDto = UserDto.builder().email(userToRegistration.getEmail()).userName(userToRegistration.getUserName()).build();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userToRegistration))
         .contentType(APPLICATION_JSON_VALUE))
         .andExpect(status().isCreated())
         .andExpect(content().contentType(APPLICATION_JSON_VALUE))
         .andExpect(content().json(asJsonString(expectedUserDto)));
   }

   @Test
   void userReturnedPrincipalWhenUserIsAuthorized() throws Exception {
      UserDto userToRegistration = UserRandomTool.randomUserDto();
      this.mockMvc.perform(get("/user").principal(userToRegistration::getEmail))
         .andExpect(status().isOk())
         .andExpect(content().string(""));
   }

}
