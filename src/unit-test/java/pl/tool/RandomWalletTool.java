package pl.tool;

import pl.user.UserDto;
import pl.wallet.WalletDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;


public class RandomWalletTool {
   public static WalletDto randomWalletDto() {
      return RandomWalletTool.randomWalletDtoBuilder().build();
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
         .users(new ArrayList<>(Arrays.asList(owner)))
         .build();
   }
}
