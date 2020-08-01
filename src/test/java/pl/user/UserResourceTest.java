package pl.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.tool.Tool;
import pl.tool.UriTool;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.tool.UserTool.*;
import static pl.tool.JsonTool.parseJson;
import static pl.tool.UriTool.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_register_user() throws Exception {
        UserDto userDto = randomUserDto();
        UserDto expectedResponse = UserDto.builder()
                .email(userDto.getEmail())
                .build();
        mockMvc.perform(put(register())
                .content(parseJson(userDto))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(parseJson(expectedResponse)));
    }

    @Test
    void should_return_principal() throws Exception {
        UserDto user = registerRandomUser(mockMvc);
        mockMvc.perform(get(userPrincipal()).with(user(user.getEmail()).password(user.getPassword())))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"authorities\": [\n" +
                        "    {\n" +
                        "      \"authority\": \"ROLE_USER\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"details\": null,\n" +
                        "  \"authenticated\": true,\n" +
                        "  \"principal\": {\n" +
                        "    \"password\": \"" + user.getPassword() + "\",\n" +
                        "    \"username\": \"" + user.getEmail() + "\",\n" +
                        "    \"authorities\": [\n" +
                        "      {\n" +
                        "        \"authority\": \"ROLE_USER\"\n" +
                        "      }\n" +
                        "    ],\n" +
                        "    \"accountNonExpired\": true,\n" +
                        "    \"accountNonLocked\": true,\n" +
                        "    \"credentialsNonExpired\": true,\n" +
                        "    \"enabled\": true\n" +
                        "  },\n" +
                        "  \"credentials\": \"" + user.getPassword() + "\",\n" +
                        "  \"name\": \"" + user.getEmail() + "\"\n" +
                        "}"));
    }

    @Test
    void should_return_user_property() throws Exception {
        UserDto user = registerRandomUser(mockMvc);
        UserDto expectedResponse = UserDto.builder()
                .email(user.getEmail())
                .items(new HashMap<>())
                .build();
        mockMvc.perform(get(userProperty()).with(user(user.getEmail()).password(user.getPassword())).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(parseJson(expectedResponse)));
    }

    @Test
    void should_update_user_properties() throws Exception {
        UserDto user = registerRandomUser(mockMvc);
        UserDto updatedUser = randomUserDtoBuilder().
                items(Map.of("favoriteWalletId", Tool.randomLong().toString()))
                .userName(Tool.randomString())
                .build();
        UserDto expectedResponse = UserDto.builder()
                .email(updatedUser.getEmail())
                .password(updatedUser.getPassword())
                .userName(updatedUser.getUserName())
                .items(updatedUser.getItems())
                .build();
        mockMvc.perform(post(UriTool.editUser()).with(user(user.getEmail()).password(user.getPassword()))
                .contentType(APPLICATION_JSON_VALUE)
                .content(parseJson(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().json(parseJson(expectedResponse)));
    }


}