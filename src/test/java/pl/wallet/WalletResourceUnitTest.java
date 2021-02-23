package pl.wallet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.user.UserRandomTool;
import pl.user.User;
import pl.user.UserDto;
import pl.user.UserMapper;
import pl.user.UserProvider;
import pl.user.friend_ship.FriendShipProvider;
import pl.user.friend_ship.FriendShipService;
import pl.wallet.transaction.service.TransactionService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class WalletResourceUnitTest {

  private WalletRepository walletRepository;
  private UserProvider userProvider;
  private TransactionProvider transactionProvider;
  private FriendShipProvider friendShipProvider;
  private WalletService walletService;
  private WalletResource walletResource;


  @BeforeEach
  void init() {
    walletRepository = Mockito.mock(WalletRepository.class);
    userProvider = Mockito.mock(UserProvider.class);
    transactionProvider = Mockito.mock(TransactionProvider.class);
    friendShipProvider = Mockito.mock(FriendShipProvider.class);
    walletService = new WalletService(walletRepository, userProvider, transactionProvider, friendShipProvider);
    walletResource = new WalletResource(walletService);
  }

  @Test
  void addWalletReturnWalletDtoWhenAddingWalletFinishSuccessful() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    Principal principal = userDto::getEmail;
    WalletDto walletToAdding = WalletRandomTool.randomWalletDto();
    // when
    User user = UserMapper.toEntity(userDto);
    user.setPassword(null);
    when(userProvider.get(any())).thenReturn(user);
    when(walletRepository.save(any())).thenAnswer(invocation -> {
      Wallet wallet = invocation.getArgument(0, Wallet.class);
      wallet.setId(1L);
      return wallet;
    });
    when(userProvider.save(any())).thenReturn(null);
    ResponseEntity<WalletDto> walletDtoResponseEntity = walletResource.addWallet(principal, walletToAdding);
    // then
    WalletDto expectedWalletDto = WalletDto.builder()
      .id(1L)
      .name(walletToAdding.getName())
      .owner(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())
      .users(new HashSet<>(Set.of(
        UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())))
      .balance(walletToAdding.getBalance())
      .build();
    assertEquals(HttpStatus.CREATED, walletDtoResponseEntity.getStatusCode());
    assertEquals(expectedWalletDto, walletDtoResponseEntity.getBody());
  }

  @Test
  void getWalletsReturnUserWalletsWhenUserIsAuthenticated() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    Set<Wallet> wallets = Stream.iterate(0, i -> i + 1).limit(10).map(i -> WalletRandomTool.randomWallet()).collect(Collectors.toSet());
    User user = UserMapper.toEntity(userDto);
    wallets.forEach(w -> {
      w.setOwner(user);
      w.addUser(user);
    });
    // when
    when(walletRepository.getByUser(any())).thenReturn(wallets);
    Principal principal = userDto::getEmail;
    ResponseEntity<List<WalletDto>> userWalletsResponseEntity = walletResource.getWallets(principal);
    // then
    Set<WalletDto> expectedWallets = wallets.stream().map(WalletMapper::toDto).collect(Collectors.toSet());
    assertEquals(HttpStatus.OK, userWalletsResponseEntity.getStatusCode());
    Assertions.assertEquals(expectedWallets, new HashSet<>(userWalletsResponseEntity.getBody()));
  }

  @Test
  void editWalletReturnEditedWalletWhenNewWallet() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    User user = User.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build();
    Wallet wallet = WalletRandomTool.randomWallet(userDto);
    wallet.setId(1L);
    wallet.setOwner(user);
    wallet.addUser(user);
    WalletDto walletWithUpdatedFields = WalletRandomTool.randomWalletDto();
    // when
    Principal principal = userDto::getEmail;
    when(userProvider.get(any())).thenReturn(user);
    when(walletRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(wallet));
    when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, wallet.getClass()));
    ResponseEntity<WalletDto> editedWalletResponseEntity = walletResource.editWallet(principal, wallet.getId(), walletWithUpdatedFields);
    // then
    WalletDto expectedWalletDto = WalletDto.builder()
      .name(walletWithUpdatedFields.getName())
      .id(wallet.getId())
      .owner(UserDto.builder().userName(userDto.getUserName()).email(user.getEmail()).build())
      .users(Set.of(
        UserDto.builder().userName(userDto.getUserName()).email(user.getEmail()).build()
      ))
      .balance(wallet.getBalance()).build();
    assertEquals(HttpStatus.OK, editedWalletResponseEntity.getStatusCode());
    assertEquals(expectedWalletDto, editedWalletResponseEntity.getBody());
  }

  @Test
  void removeWalleReturnOnlyHttpStatusWhenWalletIsOnUserOwn() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    User user = User.builder().email(userDto.getEmail()).build();
    Wallet wallet = WalletRandomTool.randomWallet(userDto);
    wallet.setId(1L);
    wallet.setOwner(user);
    wallet.addUser(user);
    // when
    when(walletRepository.findByIdAndUserEmail(any(), any())).thenReturn(Optional.of(wallet));
    Principal principal = userDto::getEmail;
    ResponseEntity removedWalletResponseEntity = walletResource.removeWallet(principal, wallet.getId());
    // then
    assertEquals(HttpStatus.NO_CONTENT, removedWalletResponseEntity.getStatusCode());
    assertEquals(null, removedWalletResponseEntity.getBody());
  }

  @Test
  void getWalletReturnWalletWhenIsOnUserOwn() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    User user = User.builder().email(userDto.getEmail()).build();
    Wallet wallet = WalletRandomTool.randomWallet(userDto);
    wallet.setId(1L);
    wallet.setOwner(user);
    wallet.addUser(user);
    // when
    when(userProvider.get(any())).thenReturn(user);
    when(walletRepository.findByIdAndUserEmail(any(), any())).thenReturn(Optional.of(wallet));
    Principal principal = userDto::getEmail;
    ResponseEntity<WalletDto> walletDtoResponseEntity = walletResource.getWallet(principal, wallet.getId());
    // then
    WalletDto expectedWalletDto = WalletMapper.toDto(wallet);
    assertEquals(HttpStatus.OK, walletDtoResponseEntity.getStatusCode());
    assertEquals(expectedWalletDto, walletDtoResponseEntity.getBody());
  }

  @Test
  void shareWalletWithFriendAddingFriendToWalletUsersWhenWalletIsOnCallUserOwn() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    UserDto friendDto = UserRandomTool.randomUserDto();
    User user = User.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build();
    User friend = User.builder().email(friendDto.getEmail()).userName(friendDto.getUserName()).build();
    Wallet wallet = WalletRandomTool.randomWallet(userDto);
    wallet.setId(1L);
    wallet.setOwner(user);
    wallet.addUser(user);
    // when
    when(userProvider.get(any())).thenReturn(user, friend);
    when(friendShipProvider.isFriends(any(), any())).thenReturn(true);
    when(walletRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(wallet));
    when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Wallet.class));
    Principal principal = userDto::getEmail;
    ResponseEntity<WalletDto> walletDtoResponseEntity = walletResource.shareWalletWithFriend(principal, wallet.getId(), friendDto.getEmail());
    // then
    WalletDto expectedWalletDto = getWalletDtoBuilder(WalletMapper.toDto(wallet))
      .owner(UserDto.builder().email(user.getEmail()).userName(user.getUserName()).build())
      .users(Set.of(
        UserDto.builder()
          .email(friendDto.getEmail())
          .userName(friendDto.getUserName())
          .build(),
        UserDto.builder()
          .email(userDto.getEmail())
          .userName(userDto.getUserName())
          .build()))
      .build();
    assertEquals(HttpStatus.OK, walletDtoResponseEntity.getStatusCode());
    assertEquals(expectedWalletDto, walletDtoResponseEntity.getBody());
  }

  @Test
  void changeWalletOwnerReturnWalletDtoWithNewOwnerWhenUserIsOwnerAnd() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    UserDto newOwnerDto = UserRandomTool.randomUserDto();
    User user = User.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build();
    User newOwner = User.builder().email(newOwnerDto.getEmail()).userName(newOwnerDto.getUserName()).build();
    Wallet wallet = WalletRandomTool.randomWallet(userDto);
    wallet.setId(1L);
    wallet.setOwner(user);
    wallet.addUser(user);
    wallet.addUser(newOwner);
    // when
    when(userProvider.get(any())).thenReturn(user, newOwner);
    when(friendShipProvider.isFriends(any(), any())).thenReturn(true);
    when(walletRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(wallet));
    when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Wallet.class));
    Principal principal = userDto::getEmail;
    ResponseEntity<WalletDto> walletDtoResponseEntity = walletResource.changeWalletOwner(principal, wallet.getId(), newOwnerDto.getEmail());
    // then
    WalletDto expectedWalletDto = getWalletDtoBuilder(WalletMapper.toDto(wallet))
      .owner(UserDto.builder().userName(newOwnerDto.getUserName())
        .email(newOwnerDto.getEmail()).build()).users(Set.of(
        UserDto.builder()
          .userName(userDto.getUserName())
          .email(userDto.getEmail())
          .build(),
        UserDto.builder()
          .userName(newOwner.getUserName())
          .email(newOwnerDto.getEmail()).build()))
      .build();
    assertEquals(HttpStatus.OK, walletDtoResponseEntity.getStatusCode());
    assertEquals(expectedWalletDto, walletDtoResponseEntity.getBody());
  }

  @Test
  void removeUserFromWalletReturnWalletDtoWhenRemovingUserIsNotOwner() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    UserDto removingUserDto = UserRandomTool.randomUserDto();
    User user = User.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build();
    User removingUser = User.builder().email(removingUserDto.getEmail()).userName(removingUserDto.getUserName()).build();
    Wallet wallet = WalletRandomTool.randomWallet(userDto);
    wallet.setId(1L);
    wallet.setOwner(user);
    wallet.addUser(user);
    wallet.addUser(removingUser);
    // when
    when(userProvider.get(any())).thenReturn(user, removingUser);
    when(friendShipProvider.isFriends(any(), any())).thenReturn(true);
    when(walletRepository.findByIdAndOwner(any(), any())).thenReturn(Optional.of(wallet));
    when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Wallet.class));
    Principal principal = userDto::getEmail;
    ResponseEntity<WalletDto> walletDtoResponseEntity = walletResource.removeFriendFromWallet(principal, wallet.getId(), removingUserDto.getEmail());
    // then
    WalletDto expectedWalletDto = getWalletDtoBuilder(WalletMapper.toDto(wallet)).owner(UserDto.builder().userName(userDto.getUserName()).email(userDto.getEmail()).build()).users(Set.of(UserDto.builder().email(userDto.getEmail()).userName(userDto.getUserName()).build())).build();
    assertEquals(HttpStatus.OK, walletDtoResponseEntity.getStatusCode());
    assertEquals(expectedWalletDto, walletDtoResponseEntity.getBody());
  }

  private WalletDto.WalletDtoBuilder getWalletDtoBuilder(WalletDto walletDto) {
    return WalletDto.builder().id(walletDto.getId()).name(walletDto.getName()).balance(walletDto.getBalance()).users(walletDto.getUsers()).owner(walletDto.getOwner());
  }


}
