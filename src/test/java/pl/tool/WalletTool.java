package pl.tool;

import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import pl.user.UserDto;
import pl.wallet.WalletDto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static pl.tool.JsonTool.parseJson;
import static pl.tool.JsonTool.parseObject;

public class WalletTool {
    public static WalletDto randomWalletDto() {
        return WalletTool.randomWalletDtoBuilder().build();
    }

    private static WalletDto.WalletDtoBuilder randomWalletDtoBuilder() {
        return WalletDto.builder()
                .name(Tool.randomString())
                .balance(Tool.randomBigDecimal());
    }


    public static WalletDto addRandomWallet(MockMvc mockMvc, UserDto user) throws Exception {
        return (WalletDto) parseObject(mockMvc.perform(put(UriTool.addWallet()).with(user(user.getEmail()).password(user.getPassword())).contentType(MediaType.APPLICATION_JSON_VALUE).content(parseJson(WalletTool.randomWalletDto()))).andReturn().getResponse().getContentAsString(), WalletDto.class);
    }
}
