package pl.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.user.User;
import pl.user.UserDto;
import pl.user.UserRandomTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Profile.TEST_MVC_PROFILE_NAME;
import static pl.tool.JsonTool.asJsonString;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(TEST_MVC_PROFILE_NAME)
public class WalletResourceMvcTest {
   @Autowired
   private MockMvc mockMvc;

   @Test
   void addWalletReturnedResponseEntityWithWalletDtoWhenWalletDtoIsValid() throws Exception {
      // given
      WalletDto walletDto = WalletRandomTool.randomWalletDto();
      UserDto userDto = UserRandomTool.randomUserDto();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userDto))
         .contentType(APPLICATION_JSON_VALUE));
      // when
      // then
      WalletDto expectedWalletDto = WalletDto.builder()
         .id(2L)
         .name(walletDto.getName())
         .owner(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())
         .users(new HashSet<>(Set.of(UserDto.builder().userName(userDto.getUserName()).email(userDto.getEmail()).build())))
         .balance(walletDto.getBalance())
         .build();
      mockMvc.perform(put("/wallet/add").with(user(userDto.getEmail()).password(userDto.getPassword()))
         .contentType(MediaType.APPLICATION_JSON_VALUE)
         .content(asJsonString(walletDto)))
         .andExpect(content().contentType(APPLICATION_JSON_VALUE))
         .andExpect(status().isCreated())
         .andExpect(content().json(asJsonString(expectedWalletDto)));
   }

   @Test
   void getWalletReturnedAllUserWallets() throws Exception {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userDto))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/register")
         .content(asJsonString(UserRandomTool.randomUserDto()))
         .contentType(APPLICATION_JSON_VALUE));
      List<WalletDto> expectedWalletsDto = List.of(WalletDto.builder()
         .id(1L)
         .name("Wallet")
         .owner(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())
         .users(new HashSet<>(Set.of(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())))
         .balance(BigDecimal.valueOf(0.0))
         .build());
      // when
      // then
      mockMvc.perform(get("/wallet").with(user(userDto.getEmail()).password(userDto.getPassword())))
         .andExpect(status().isOk())
         .andExpect(content().json(asJsonString(expectedWalletsDto)));
   }

   @Test
   void editWalletReturnedUpdatedWalletDto() throws Exception {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userDto))
         .contentType(APPLICATION_JSON_VALUE));
      WalletDto walletDtoWithUpdatedName = WalletRandomTool.randomWalletDto();
      WalletDto expectedWalletDto = WalletDto.builder()
         .id(1L)
         .name(walletDtoWithUpdatedName.getName())
         .owner(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())
         .users(new HashSet<>(Set.of(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())))
         .balance(BigDecimal.valueOf(0.0))
         .build();
      // when
      // then
      mockMvc.perform(post("/wallet/edit/1")
         .contentType(APPLICATION_JSON_VALUE)
         .content(asJsonString(walletDtoWithUpdatedName))
         .with(user(userDto.getEmail()).password(userDto.getPassword())))
         .andDo(print())
         .andExpect(status().isOk())
         .andExpect(content().json(asJsonString(expectedWalletDto)));
   }

//    @Test
//    void removeWalletReturnedNoContentWhenRemovedSuccess() throws Exception {
//        // given
//        UserDto userDto = UserRandomTool.randomUserDto();
//        this.mockMvc.perform(delete("/register"));
//        // when
//        // then
//        mockMvc.perform(put("/wallet/remove/1").with(user(userDto.getEmail()).password(userDto.getPassword())))
//                .andExpect(status().isNoContent());
//    }

   @Test
   void getWalletReturnedWalletDtoWhenIsUserWallet() throws Exception {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userDto))
         .contentType(APPLICATION_JSON_VALUE));
      WalletDto expectedWalletDto = WalletDto.builder()
         .id(1L)
         .name("Wallet")
         .owner(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())
         .users(new HashSet<>(Set.of(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())))
         .balance(BigDecimal.valueOf(0.0))
         .build();
      // when
      // then
      this.mockMvc.perform(get("/wallet/1")
         .content(asJsonString(userDto))
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .contentType(APPLICATION_JSON_VALUE))
         .andExpect(status().isOk())
         .andExpect(content().json(asJsonString(expectedWalletDto)));
   }

   @Test
   void shareWalletWithFriendReturnedWalletDtoWhenIsUserWalletAndUserHasFriend() throws Exception {
      //given
      UserDto userDto = UserRandomTool.randomUserDto();
      UserDto userFriendDto = UserRandomTool.randomUserDto();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userDto))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userFriendDto))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/invite")
         .content(asJsonString(userFriendDto.getEmail()))
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/friend/add/1").with(user(userFriendDto.getEmail()).password(userFriendDto.getPassword())))
         .andExpect(status().isCreated())
         .andExpect(content().contentType(APPLICATION_JSON_VALUE));
      WalletDto expectedWalletDto = WalletDto.builder()
         .id(1L)
         .name("Wallet")
         .owner(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())
         .users(new HashSet<>(Set.of(
            UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build(),
            UserDto.builder().email(userFriendDto.getEmail()).userName(userFriendDto.getUserName()).build()
         )))
         .balance(BigDecimal.valueOf(0.0))
         .build();
      // when
      // then
      this.mockMvc.perform(post("/wallet/1/add_friend_to_wallet")
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .content(asJsonString(userFriendDto.getEmail()))
         .contentType(APPLICATION_JSON_VALUE))
         .andExpect(status().isOk())
         .andExpect(content().json(asJsonString(expectedWalletDto)));
   }

   @Test
   void changeOwnerReturnedWalletDtoWhenUserIsOwnerAndSecondUserIsAddedToWallet() throws Exception {
      //given
      UserDto userDto = UserRandomTool.randomUserDto();
      UserDto userFriendDto = UserRandomTool.randomUserDto();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userDto))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userFriendDto))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/invite")
         .content(asJsonString(userFriendDto.getEmail()))
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/friend/add/1").with(user(userFriendDto.getEmail()).password(userFriendDto.getPassword())))
         .andExpect(status().isCreated())
         .andExpect(content().contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(post("/wallet/1/add_friend_to_wallet")
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .content(asJsonString(userFriendDto.getEmail()))
         .contentType(APPLICATION_JSON_VALUE));
      WalletDto expectedWalletDto = WalletDto.builder()
         .id(1L)
         .name("Wallet")
         .owner(UserDto.builder().email(userFriendDto.getEmail()).userName(userFriendDto.getUserName()).build())
         .users(new HashSet<>(Set.of(
            UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build(),
            UserDto.builder().email(userFriendDto.getEmail()).userName(userFriendDto.getUserName()).build()
         )))
         .balance(BigDecimal.valueOf(0.0))
         .build();
      // when
      // then
      this.mockMvc.perform(post("/wallet/1/change_owner")
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .contentType(APPLICATION_JSON_VALUE)
         .content(userFriendDto.getEmail()))
         .andExpect(status().isOk())
         .andExpect(content().contentType(APPLICATION_JSON_VALUE))
         .andExpect(content().json(asJsonString(expectedWalletDto)));
   }

   @Test
   void removeFriendFromWalletReturnedWalletDtoWhenUserRemoveCorrectly() throws Exception {
      //given
      UserDto userDto = UserRandomTool.randomUserDto();
      UserDto userFriendDto = UserRandomTool.randomUserDto();
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userDto))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/register")
         .content(asJsonString(userFriendDto))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/invite")
         .content(asJsonString(userFriendDto.getEmail()))
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .contentType(APPLICATION_JSON_VALUE));
      this.mockMvc.perform(put("/friend/add/1")
         .with(user(userFriendDto.getEmail()).password(userFriendDto.getPassword())));
      this.mockMvc.perform(post("/wallet/1/add_friend_to_wallet")
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .content(asJsonString(userFriendDto.getEmail()))
         .contentType(APPLICATION_JSON_VALUE));
      WalletDto expectedWalletDto = WalletDto.builder()
         .id(1L)
         .name("Wallet")
         .owner(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())
         .users(new HashSet<>(Set.of(
            UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build()
         )))
         .balance(BigDecimal.valueOf(0.0))
         .build();
      // when
      // then
      this.mockMvc.perform(delete("/wallet/1/remove_friend_from_wallet")
         .with(user(userDto.getEmail()).password(userDto.getPassword()))
         .content(userFriendDto.getEmail()).contentType(APPLICATION_JSON_VALUE))
         .andExpect(status().isOk())
         .andExpect(content().json(asJsonString(expectedWalletDto)));
   }
}
