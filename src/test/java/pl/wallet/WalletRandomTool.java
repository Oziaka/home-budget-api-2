package pl.wallet;

import pl.tool.RandomUtils;
import pl.user.User;
import pl.user.UserDto;
import pl.user.UserMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;


public class WalletRandomTool {
    public static WalletDto randomWalletDto() {
        return WalletRandomTool.randomWalletDtoBuilder().build();
    }

    private static WalletDto.WalletDtoBuilder randomWalletDtoBuilder() {
        return WalletDto.builder()
          .name(RandomUtils.randomString())
          .balance(RandomUtils.randomBigDecimal());
    }


    public static WalletDto defaultWallet(UserDto user) {
        UserDto owner = UserDto.builder().email(user.getEmail()).userName(user.getUserName()).build();
        return WalletDto.builder()
          .name("Wallet")
          .balance(BigDecimal.valueOf(0.0))
          .owner(owner)
          .users(new HashSet<>(Collections.singleton(owner)))
          .build();
    }

    public static Wallet randomWallet() {
        return randomWalletBuilder().build();
    }

    private static Wallet.WalletBuilder randomWalletBuilder() {
        return Wallet.builder()
          .name(RandomUtils.randomString())
          .balance(RandomUtils.randomBigDecimal());
    }

    public static Wallet randomWallet(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setPassword(null);
        return randomWallet(user);
    }


    public static Wallet randomWallet(User user) {
        return randomWalletBuilder().owner(user).users(new HashSet<>(Collections.singletonList(user))).build();
    }

    public static WalletDto randomWalletDto(UserDto userDto) {
        return randomWalletDtoBuilder().owner(userDto).users(new HashSet<>(Collections.singletonList(userDto))).build();
    }

}
