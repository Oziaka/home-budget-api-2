package pl.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.Profile.TEST_MVC_PROFILE_NAME;
import static pl.tool.JsonTool.asJsonString;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(TEST_MVC_PROFILE_NAME)
public class UserResouerceMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerReturnUserDtoWhenRegisteredSuccessful() throws Exception {
        UserDto userToRegistration = UserRandomTool.randomUserDto();
        UserDto expectedUserDto = UserDto.builder().email(userToRegistration.getEmail()).userName(userToRegistration.getUserName()).build();
        this.mockMvc.perform(post("/api/register")
            .content(asJsonString(userToRegistration))
            .contentType(APPLICATION_JSON_VALUE))
          .andExpect(status().isCreated())
          .andExpect(content().contentType(APPLICATION_JSON_VALUE))
          .andExpect(content().json(asJsonString(expectedUserDto)));
    }

    @Test
    void userReturnedPrincipalWhenUserIsAuthorized() throws Exception {
        UserDto user = UserRandomTool.randomUserDto();
        this.mockMvc.perform(get("/api/user").with(user(user.getEmail()).password(user.getPassword())))
          .andExpect(status().isOk());
    }

    @Test
    void loginRedirectWhenIsSuccessfulLogged() throws Exception {
        UserDto user = UserRandomTool.randomUserDto();
        this.mockMvc.perform(post("/api/register")
          .content(asJsonString(user))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(post("/api/login").content(asJsonString(user)))
          .andExpect(status().is(302))
          .andExpect(content().string(""));

    }

    @Test
    void editUserReturnedUserDtoWithEditedValue() throws Exception {
        UserDto user = UserRandomTool.randomUserDto();
        UserDto userWithNewValue = UserRandomTool.randomUserDto();
        UserDto expectedUserDto = UserDto.builder().email(userWithNewValue.getEmail()).userName(userWithNewValue.getUserName()).items(new HashMap<>()).build();
        this.mockMvc.perform(post("/api/register")
          .content(asJsonString(user))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/api/user/edit").with(user(user.getEmail()).password(user.getPassword()))
            .content(asJsonString(userWithNewValue))
            .contentType(APPLICATION_JSON_VALUE))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().json(asJsonString(expectedUserDto)));
    }
}
