package pl.tool;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.tool.UriTool;
import pl.user.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static pl.tool.JsonTool.parseJson;
import static pl.tool.Tool.*;

public class UserTool {

    public static UserDto randomUserDto() {
        return randomUserDtoBuilder().build();
    }

    public static UserDto.UserDtoBuilder randomUserDtoBuilder() {
        return UserDto.builder()
                .email(randomEmaiL())
                .password(randomString());
    }

    private static String randomEmaiL() {
        return randomString() + randomString(3) + '@' + randomString(2);
    }

    public static UserDto registerRandomUser(MockMvc mockMvc) throws Exception {
        UserDto user = randomUserDto();
        mockMvc.perform(MockMvcRequestBuilders.put(UriTool.register()).content(parseJson(user)).contentType(MediaType.APPLICATION_JSON_VALUE));
        return user;
    }
}
