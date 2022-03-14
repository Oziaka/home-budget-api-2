package pl.wallet;

import pl.user.UserMapper;
import pl.wallet.currency.CurrencyMapper;

import java.util.stream.Collectors;

public class WalletMapper {
    private WalletMapper() {
    }

    public static WalletDto toDto(Wallet wallet) {
        return WalletDto.builder()
          .id(wallet.getId())
          .balance(wallet.getBalance())
          .name(wallet.getName())
          .users(wallet.getUsers().stream().map(UserMapper::toDto).collect(Collectors.toSet()))
          .owner(UserMapper.toDto(wallet.getOwner()))
          .currencyDto(CurrencyMapper.toDto(wallet.getCurrency()))
          .build();
    }

    public static Wallet toEntity(WalletDto walletDto) {
        Wallet wallet = new Wallet();
        wallet.setName(walletDto.getName());
        wallet.setBalance(walletDto.getBalance());
        wallet.setCurrency(CurrencyMapper.toEntity(walletDto.getCurrencyDto()));
        return wallet;
    }

}
