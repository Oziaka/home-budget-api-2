package pl.wallet;

public class WalletMapper {
    private WalletMapper() {
    }

    public static WalletDto toDto(Wallet wallet) {
        return WalletDto.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .name(wallet.getName())
                .build();
    }

    public static Wallet toEntity(WalletDto walletDto) {
        Wallet wallet = new Wallet();
        wallet.setName(walletDto.getName());
        wallet.setBalance(walletDto.getBalance());
        return wallet;
    }

}
