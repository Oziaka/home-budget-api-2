package pl.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;
import pl.user.UserProvider;
import pl.user.friend_ship.FriendShipProvider;
import pl.wallet.transaction.provider.TransactionProvider;
import pl.wallet.transaction.provider.TransactionRecurringProvider;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserProvider userProvider;
    private final TransactionProvider transactionProvider;
    private final FriendShipProvider friendShipProvider;
    private final TransactionRecurringProvider transactionRecurringProvider;

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

    void removeWallet(Principal principal, Long walletId) {
        User user = userProvider.get(principal);
        Optional<Wallet> walletOptional = walletRepository.findByIdAndOwner(walletId, user);
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            walletOptional.get().getUsers().forEach(tmpUser -> {
                tmpUser.setWallets(tmpUser.getWallets().stream().filter(w -> !w.equals(wallet)).collect(Collectors.toSet()));
                userProvider.save(tmpUser);
            });
            wallet.getTransactions().forEach(t->transactionProvider.remove(t.getId()));
            wallet.getTransactionsRecurring().forEach(t->transactionRecurringProvider.remove(t.getId()));
            remove(walletId);
        } else
            throw new WalletException(WalletError.NOT_YOUR_PROPERTY);
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
            throw new WalletException(WalletError.NEW_OWNER_SHOULD_BY_IN_WALLET_USERS);
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
        if (owner.equals(friendToRemoveFromWallet))
            throw new WalletException(WalletError.CAN_NOT_REMOVE_OWNER_FROM_WALLET);
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
        return walletRepository.findByIdAndUserEmail(walletId, email).orElseThrow(() -> new WalletException(WalletError.NOT_YOUR_PROPERTY));
    }

    private Wallet getByOwner(User owner, Long walletId) {
        return walletRepository.findByIdAndOwner(walletId, owner).orElseThrow(() -> new WalletException(WalletError.NOT_YOUR_PROPERTY));
    }

    private Wallet getWallet(User user, Long id) {
        return walletRepository.findByIdAndUserEmail(id, user.getEmail()).orElseThrow(() -> new WalletException(WalletError.NOT_YOUR_PROPERTY));
    }
}
