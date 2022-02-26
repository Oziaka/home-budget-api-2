package pl.user.friend_ship;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.user.UserDto;
import pl.user.UserRandomTool;
import pl.user.friend_ship.friend.FriendDto;

import java.util.Collections;
import java.util.List;

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
public class FriendShipResourceMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addFriendReturnedFriendShipDto() throws Exception {
        // given
        UserDto userDto = UserRandomTool.randomUserDto();
        UserDto invitedDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(put("/register")
          .content(asJsonString(userDto))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/register")
          .content(asJsonString(invitedDto))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/invite")
          .content(asJsonString(invitedDto.getEmail()))
          .with(user(userDto.getEmail()).password(userDto.getPassword()))
          .contentType(APPLICATION_JSON_VALUE));
        FriendShipDto expectedFriendShipDto = FriendShipDto.builder()
          .id(1L).user(UserDto.builder().email(invitedDto.getEmail()).userName(invitedDto.getUserName()).build())
          .user2(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())
          .build();
        // when
        // then
        this.mockMvc.perform(put("/friend/add/1").with(user(invitedDto.getEmail()).password(invitedDto.getPassword())))
          .andExpect(status().isCreated())
          .andExpect(content().contentType(APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.id").value(expectedFriendShipDto.getId()))
          .andExpect(jsonPath("$.user.email").value(expectedFriendShipDto.getUser().getEmail()))
          .andExpect(jsonPath("$.user.userName").value(expectedFriendShipDto.getUser().getUserName()))
          .andExpect(jsonPath("$.user2.email").value(expectedFriendShipDto.getUser2().getEmail()))
          .andExpect(jsonPath("$.user2.userName").value(expectedFriendShipDto.getUser2().getUserName()))
          .andExpect(jsonPath("$.dateOfAdding").isNotEmpty());
    }

    @Test
    void getFriendsReturnListOfFriendShipDto() throws Exception {
        // given
        UserDto userDto = UserRandomTool.randomUserDto();
        UserDto invitedDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(put("/register")
          .content(asJsonString(userDto))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/register")
          .content(asJsonString(invitedDto))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/invite")
          .content(asJsonString(invitedDto.getEmail()))
          .with(user(userDto.getEmail()).password(userDto.getPassword()))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/friend/add/1").with(user(invitedDto.getEmail()).password(invitedDto.getPassword())))
          .andExpect(status().isCreated())
          .andExpect(content().contentType(APPLICATION_JSON_VALUE));
        List<FriendDto> expectedFriendDto = Collections.singletonList(FriendDto.builder().friend(userDto).friendShipId(1L).build());
        List<FriendDto> expectedFriendDto2 = Collections.singletonList(FriendDto.builder().friend(invitedDto).friendShipId(2L).build());
        // when
        // then
        this.mockMvc.perform(get("/friend").with(user(invitedDto.getEmail()).password(invitedDto.getPassword())))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$[0].friendShipId").value(expectedFriendDto.get(0).getFriendShipId()))
          .andExpect(jsonPath("$[0].friend.email").value(expectedFriendDto.get(0).getFriend().getEmail()))
          .andExpect(jsonPath("$[0].friend.userName").value(expectedFriendDto.get(0).getFriend().getUserName()))
          .andExpect(jsonPath("$[0].dateOfAdding").isNotEmpty());
        this.mockMvc.perform(get("/friend").with(user(userDto.getEmail()).password(userDto.getPassword())))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$[0].friendShipId").value(expectedFriendDto2.get(0).getFriendShipId()))
          .andExpect(jsonPath("$[0].friend.email").value(expectedFriendDto2.get(0).getFriend().getEmail()))
          .andExpect(jsonPath("$[0].friend.userName").value(expectedFriendDto2.get(0).getFriend().getUserName()))
          .andExpect(jsonPath("$[0].dateOfAdding").isNotEmpty());
    }

    @Test
    void removeFriendReturnedStatusNoContentWhenRemovingSuccess() throws Exception {
        // given
        UserDto userDto = UserRandomTool.randomUserDto();
        UserDto invitedDto = UserRandomTool.randomUserDto();
        this.mockMvc.perform(put("/register")
          .content(asJsonString(userDto))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/register")
          .content(asJsonString(invitedDto))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/invite")
          .content(asJsonString(invitedDto.getEmail()))
          .with(user(userDto.getEmail()).password(userDto.getPassword()))
          .contentType(APPLICATION_JSON_VALUE));
        this.mockMvc.perform(put("/friend/add/1").with(user(invitedDto.getEmail()).password(invitedDto.getPassword())))
          .andExpect(status().isCreated())
          .andExpect(content().contentType(APPLICATION_JSON_VALUE));
        // when
        // then
        this.mockMvc.perform(delete("/friend/remove/2").with(user(userDto.getEmail()).password(userDto.getPassword())))
          .andExpect(status().isNoContent());
    }
}
