package pl.tool;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.user.UserDto;
import pl.wallet.WalletDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static pl.tool.JsonMapper.parseJson;
import static pl.tool.JsonMapper.parseObject;
import static pl.tool.UriPath.*;


public class RandomWalletTool {
   public static WalletDto randomWalletDto() {
      return RandomWalletTool.randomWalletDtoBuilder().build();
   }

   private static WalletDto.WalletDtoBuilder randomWalletDtoBuilder() {
      return WalletDto.builder()
         .name(RandomUtils.randomString())
         .balance(RandomUtils.randomBigDecimal());
   }


   public static WalletDto addRandomWallet(MockMvc mockMvc, UserDto user) throws Exception {
      return parseObject(mockMvc.perform(put(addWallet()).with(user(user.getEmail()).password(user.getPassword())).contentType(MediaType.APPLICATION_JSON_VALUE).content(parseJson(RandomWalletTool.randomWalletDto()))).andReturn().getResponse().getContentAsString()
         , new WalletDto());
   }

   public static WalletDto defaultWallet(UserDto user) {
      UserDto owner = UserDto.builder().email(user.getEmail()).userName(user.getUserName()).build();
      return WalletDto.builder()
         .name("Wallet")
         .balance(BigDecimal.valueOf(0.0))
         .owner(owner)
         .users(new ArrayList<>(Arrays.asList(owner)))
         .build();
   }
}
