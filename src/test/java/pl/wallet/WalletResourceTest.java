package pl.wallet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.tool.JsonMapper;
import pl.tool.RandomUserTool;
import pl.tool.RandomWalletTool;
import pl.user.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.tool.JsonMapper.parseJson;
import static pl.tool.UriPath.*;

@SpringBootTest
@AutoConfigureMockMvc
class WalletResourceTest {

   @Autowired
   private MockMvc mockMvc;

   @Test
   void addWalletReturnWalletWhenSenTWalletIsCorrect() throws Exception {
      // given
      UserDto user = RandomUserTool.registerRandomUser(mockMvc);
      WalletDto wallet = RandomWalletTool.randomWalletDto();
      // when
      ResultActions result = mockMvc.perform(put(addWallet())
         .with(user(user.getEmail()).password(user.getPassword()))
         .contentType(MediaType.APPLICATION_JSON_VALUE)
         .content(parseJson(wallet)));
      // then
      WalletDto expectedResponse = wallet;
      WalletDto response = JsonMapper.parseObject(result
         .andExpect(status().isCreated())
         .andReturn().getResponse().getContentAsString(), new WalletDto());
      assertThat(response).isEqualToIgnoringNullFields(expectedResponse);
      assertThat(response).extracting(WalletDto::getId).isNotNull();
   }

   @Test
   void getWalletsReturnAllUserWallets() throws Exception {
      // given
      UserDto user = RandomUserTool.registerRandomUser(mockMvc);
      WalletDto wallet = RandomWalletTool.addRandomWallet(mockMvc, user);
      // when
      ResultActions result = mockMvc.perform(get(getWallets())
         .with(user(user.getEmail()).password(user.getPassword())));
      // then
      List<WalletDto> expectedResponse = new ArrayList<>();
      expectedResponse.add(RandomWalletTool.defaultWallet(user));
      expectedResponse.add(wallet);
      result.andExpect(status().isOk())
         .andDo(print())
         .andExpect(content().contentType(MediaType.APPLICATION_JSON))
         .andExpect(jsonPath("$").isArray())
         .andExpect(jsonPath("$.length()").value(expectedResponse.size()))
         .andExpect(jsonPath("$[0].id", anything()))
         .andExpect(jsonPath("$[0].name", is(expectedResponse.get(0).getName())))
         .andExpect(jsonPath("$[0].balance", is(expectedResponse.get(0).getBalance().doubleValue())))
         .andExpect(jsonPath("$[0].owner.id").isEmpty())
         .andExpect(jsonPath("$[0].owner.email", is(user.getEmail())))
         .andExpect(jsonPath("$[0].owner.password").isEmpty())
         .andExpect(jsonPath("$[0].owner.items").isEmpty())
         .andExpect(jsonPath("$[0].users").isArray())
         .andExpect(jsonPath("$[0].users.length()").value(expectedResponse.get(0).getUsers().size()))
         .andExpect(jsonPath("$[0].users[0].id").isEmpty())
         .andExpect(jsonPath("$[0].users[0].email", is(expectedResponse.get(0).getUsers().get(0).getEmail())))
         .andExpect(jsonPath("$[0].users[0].password").isEmpty())
         .andExpect(jsonPath("$[0].users[0].items").isEmpty())

         .andExpect(jsonPath("$[1].id", anything()))
         .andExpect(jsonPath("$[1].id", anything()))
         .andExpect(jsonPath("$[1].name", is(expectedResponse.get(1).getName())))
         .andExpect(jsonPath("$[1].balance", is(expectedResponse.get(1).getBalance().doubleValue())))
         .andExpect(jsonPath("$[1].owner.id").isEmpty())
         .andExpect(jsonPath("$[1].owner.email", is(user.getEmail())))
         .andExpect(jsonPath("$[1].owner.password").isEmpty())
         .andExpect(jsonPath("$[1].owner.items").isEmpty())
         .andExpect(jsonPath("$[1].users").isArray())
         .andExpect(jsonPath("$[1].users.length()").value(expectedResponse.get(1).getUsers().size()))
         .andExpect(jsonPath("$[1].users[0].id").isEmpty())
         .andExpect(jsonPath("$[1].users[0].email", is(expectedResponse.get(1).getUsers().get(0).getEmail())))
         .andExpect(jsonPath("$[1].users[0].password").isEmpty())
         .andExpect(jsonPath("$[1].users[0].items").isEmpty());
   }
}