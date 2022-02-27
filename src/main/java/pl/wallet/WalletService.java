package pl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;
import pl.user.UserProvider;
import pl.user.friend_ship.FriendShipProvider;
import pl.wallet.transaction.provider.TransactionProvider;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WalletService {

    private WalletRepository walletRepository;
    private UserProvider userProvider;
    private TransactionProvider transactionProvider;
    private FriendShipProvider friendShipProvider;

    WalletDto addWallet(Principal principal, WalletDto walletDto) {
        User user = userProvider.get(principal);
        Wallet wallet = WalletMapper.toEntity(walletDto);
        wallet.setUsers(new HashSet<>(Collections.singleton(user)));
        wallet.setOwner(user);
        wallet = this.save(wallet);
        user.addWallet(wallet);
        userProvider.save(user);
        return WalletMapper.toDto(wallet);
    }

    List<WalletDto> getWallets(Principal principal) {
        return this.getAll(userProvider.get(principal)).stream().map(WalletMapper::toDto).collect(Collectors.toList());
    }

    private void isUserWallet(Principal principal, Long walletId) {
        this.isUserWallet(principal.getName(), walletId);
    }

    //TODO only owner should remove wallet
    void removeWallet(Principal principal, Long walletId) {
        isUserWallet(principal, walletId);
        transactionProvider.removeWalletTransactions(walletId);
        this.remove(walletId);
    }

    WalletDto getWallet(Principal principal, Long walletId) {
        User user = this.userProvider.get(principal);
        Wallet wallet = this.getWallet(user, walletId);
        return WalletMapper.toDto(wallet);
    }

    WalletDto editWallet(Principal principal, Long walletId, WalletDto walletDto) {
        User user = this.userProvider.get(principal);
        Wallet wallet = this.getByOwner(user, walletId);
        wallet.setName(walletDto.getName());
        Wallet updateWallet = this.save(wallet);
        return WalletMapper.toDto(updateWallet);
    }

    WalletDto shareWalletWithFriend(Principal principal, Long walletId, String friendUserEmail) {
        User owner = userProvider.get(principal);
        User friend = userProvider.get(friendUserEmail.replaceAll("\"", "")::toString);
        friendShipProvider.isFriends(owner, friend);
        Wallet wallet = this.getByOwner(owner, walletId);
        wallet.addUser(friend);
        Wallet savedWallet = this.save(wallet);
        friend.addWallet(savedWallet);
        userProvider.save(friend);
        return WalletMapper.toDto(savedWallet);
    }

    WalletDto changeWalletOwner(Principal principal, Long walletId, String newOwnerUserEmail) {
        User owner = userProvider.get(principal);
        User newOwner = userProvider.get(newOwnerUserEmail.replaceAll("\"", "")::toString);
        Wallet wallet = this.getByOwner(owner, walletId);
        if (!isNewOwnerAreWalletUser(wallet, newOwner))
            throw new RuntimeException("New owner have to be in wallet users");
        wallet.setOwner(newOwner);
        Wallet savedWallet = this.save(wallet);
        return WalletMapper.toDto(savedWallet);
    }

    private boolean isNewOwnerAreWalletUser(Wallet wallet, User newOwner) {
        return wallet.getUsers().stream().anyMatch(user -> user.equals(newOwner));
    }

    WalletDto removeUserFromWallet(Principal principal, Long walletId, String friendToRemoveFromWalletUserEmail) {
        User owner = userProvider.get(principal);
        User friendToRemoveFromWallet = userProvider.get(friendToRemoveFromWalletUserEmail.replace("\"", "")::toString);
        Wallet wallet = this.getByOwner(owner, walletId);
        if (owner.equals(friendToRemoveFromWallet)) throw new RuntimeException("Can not remove owner from wallet");
        wallet.removeUser(friendToRemoveFromWallet);
        Wallet savedWallet = this.save(wallet);
        return WalletMapper.toDto(savedWallet);
    }

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    private Set<Wallet> getAll(User user) {
        return walletRepository.getByUser(user);
    }

    private void remove(Long walletId) {
        walletRepository.deleteById(walletId);
    }

    public Wallet isUserWallet(String email, Long walletId) {
        return walletRepository.findByIdAndUserEmail(walletId, email).orElseThrow(()->new WalletException(WalletError.NO_YOUR_PROPERTY));
    }

    private Wallet getByOwner(User owner, Long walletId) {
        return walletRepository.findByIdAndOwner(walletId, owner).orElseThrow(()->new WalletException(WalletError.NO_YOUR_PROPERTY));
    }

    private Wallet getWallet(User user, Long id) {
        return walletRepository.findByIdAndUserEmail(id, user.getEmail()).orElseThrow(()->new WalletException(WalletError.NO_YOUR_PROPERTY));
    }
}
