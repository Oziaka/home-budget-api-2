package pl.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.tool.PrincipalInterpolator;
import pl.tool.RandomUtils;
import pl.tool.RandomUserTool;
import pl.tool.UriPath;

import java.util.Collections;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static pl.tool.JsonMapper.parseJson;
import static pl.tool.UriPath.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserResourceTest {

   @Autowired
   private MockMvc mockMvc;

   @Test
   void registerReturnUserDtoWhenRegistrationSuccessful() throws Exception {
      // given
      UserDto userDto = RandomUserTool.randomUserDto();
      // when
      ResultActions result = mockMvc.perform(put(register())
         .contentType(APPLICATION_JSON_VALUE)
         .content(parseJson(userDto)));
      // then
      UserDto expectedResponse = UserDto.builder()
         .email(userDto.getEmail())
         .build();
      result
         .andExpect(status().isCreated())
         .andExpect(content().json(parseJson(expectedResponse)));
   }

   @Test
   void userReturnPrincipalWHenUserIsAuthenticated() throws Exception {
      // given
      UserDto user = RandomUserTool.registerRandomUser(mockMvc);
      // when
      ResultActions result = mockMvc.perform(get(userPrincipal()).with(user(user.getEmail()).password(user.getPassword())));
      // then
      String expectedResponse = new PrincipalInterpolator().interpolate(user);
      result
         .andExpect(status().isOk())
         .andExpect(content().json(expectedResponse));
   }

   @Test
   void getUserReturnUserPropertyWhenUserIsAuthenticated() throws Exception {
      // given
      UserDto user = RandomUserTool.registerRandomUser(mockMvc);
      // when
      ResultActions result = mockMvc.perform(get(userProperty())
         .with(user(user.getEmail()).password(user.getPassword())));
      // then
      UserDto expectedResponse = UserDto.builder()
         .email(user.getEmail())
         .items(Collections.emptyMap())
         .build();
      result.andExpect(status().isOk())
         .andExpect(content().json(parseJson(expectedResponse)));
   }

   @Test
   void editUserReturnUserPropertyAfterUpdatedProperty() throws Exception {
      // given
      UserDto user = RandomUserTool.registerRandomUser(mockMvc);
      UserDto updatedUser = RandomUserTool.randomUserDtoBuilder().
         items(Map.of("favoriteWalletId", RandomUtils.randomLong().toString()))
         .userName(RandomUtils.randomString())
         .build();
      // when
      ResultActions result = mockMvc.perform(post(editUser()).with(user(user.getEmail()).password(user.getPassword()))
         .contentType(APPLICATION_JSON_VALUE)
         .content(parseJson(updatedUser)));
      // then
      UserDto expectedResponse = UserDto.builder()
         .email(updatedUser.getEmail())
         .userName(updatedUser.getUserName())
         .items(updatedUser.getItems())
         .build();
      result
         .andExpect(status().isOk())
         .andExpect(content().json(parseJson(expectedResponse)));
   }


}