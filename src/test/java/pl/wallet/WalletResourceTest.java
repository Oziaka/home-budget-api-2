package pl.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pl.tool.JsonTool;
import pl.tool.UriTool;
import pl.tool.UserTool;
import pl.tool.WalletTool;
import pl.user.UserDto;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tool.JsonTool.parseJson;
import static pl.tool.UriTool.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WalletResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_add_wallet() throws Exception {
        UserDto user = UserTool.registerRandomUser(mockMvc);
        WalletDto wallet = WalletTool.randomWalletDto();
        WalletDto expectedResponse = wallet;
        WalletDto response = (WalletDto) JsonTool.parseObject(mockMvc.perform(put(addWallet())
                        .with(user(user.getEmail()).password(user.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(parseJson(wallet)))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString()
                , WalletDto.class);
        assertThat(response).isEqualToIgnoringNullFields(expectedResponse);
        assertThat(response).extracting(WalletDto::getId).isNotNull();
    }

//    @Test
//    void should_get_wallets() throws Exception {
//        UserDto user = UserTool.registerRandomUser(mockMvc);
//        WalletDto wallet = WalletTool.addRandomWallet(mockMvc, user);
//        List<WalletDto> expectedResponse = new ArrayList<WalletDto>();
//        List<WalletDto> response = (List<WalletDto>) JsonTool.parseObject(mockMvc.perform(get(UriTool.getWallets())
//                        .with(user(user.getEmail()).password(user.getPassword()))
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(parseJson(wallet)))
//                        .andExpect(status().isOk())
//                        .andReturn().getResponse().getContentAsString()
//                , new TypeReference<List<WalletDto>>(){});
//    }
}