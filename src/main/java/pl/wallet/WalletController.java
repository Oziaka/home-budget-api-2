package pl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.exception.SavedEntityCanNotHaveIdException;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserService;
import pl.wallet.transaction.TransactionService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class WalletController {
    private WalletService walletService;
    private UserService userService;
    private TransactionService transactionService;

    WalletDto addWallet(Principal principal, WalletDto walletDto) {
        if (walletDto.getId() != null) throw new SavedEntityCanNotHaveIdException();
        User user = userService.getUser(principal);
        Wallet wallet = WalletMapper.toEntity(walletDto);
        wallet.setUser(user);
        wallet = walletService.saveWallet(wallet);
        return WalletMapper.toDto(wallet);
    }

    List<WalletDto> getWallets(Principal principal) {
        User user = userService.getUser(principal);
        return walletService.getWalletsByUser(user).stream().map(WalletMapper::toDto).collect(Collectors.toList());
    }

    private void isUserWallet(Principal principal, Long walletId) {
        User user = userService.getUser(principal);
        walletService.isUserWallet(user, walletId);
    }

    void removeWallet(Principal principal, Long walletId) {
        isUserWallet(principal, walletId);
        transactionService.removeWalletTransactions(walletId);
        walletService.removeWallet(walletId);
    }

    WalletDto getWallet(Principal principal, Long walletId) {
        User user = this.userService.getUser(principal);
        Wallet wallet = walletService.getUserWallet(user, walletId).orElseThrow(ThereIsNoYourPropertyException::new);
        return WalletMapper.toDto(wallet);
    }

    WalletDto editWallet(Principal principal, WalletDto walletDto) {
        User user = this.userService.getUser(principal);
        Wallet wallet = walletService.getUserWallet(user, walletDto.getId()).orElseThrow(ThereIsNoYourPropertyException::new);
        wallet.setName(walletDto.getName());
        Wallet updateWallet = walletService.saveWallet(wallet);
        return WalletMapper.toDto(updateWallet);
    }
}
