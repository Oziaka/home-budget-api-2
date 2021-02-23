package pl.wallet.transaction.resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.user.User;
import pl.user.UserProvider;
import pl.user.UserRandomTool;
import pl.wallet.Wallet;
import pl.wallet.WalletProvider;
import pl.wallet.WalletRandomTool;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryRandomTool;
import pl.wallet.category.CategoryService;
import pl.wallet.category.TransactionRandomTool;
import pl.wallet.transaction.dto.TransactionDto;
import pl.wallet.transaction.enums.Type;
import pl.wallet.transaction.model.Transaction;
import pl.wallet.transaction.model.TransactionBack;
import pl.wallet.transaction.model.TransactionLoanOrBorrow;
import pl.wallet.transaction.repository.TransactionRepository;
import pl.wallet.transaction.service.TransactionService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionResourceUnitTest {

  private TransactionResource transactionResource;
  private TransactionService transactionService;
  private TransactionRepository transactionRepository;
  private UserProvider userProvider;
  private WalletProvider walletProvider;
  private CategoryService categoryService;

  @BeforeEach
  void init() {
    transactionRepository = mock(TransactionRepository.class);
    userProvider = mock(UserProvider.class);
    walletProvider = mock(WalletProvider.class);
    categoryService = mock(CategoryService.class);
    transactionService = new TransactionService(transactionRepository, userProvider, walletProvider, categoryService);
    transactionResource = new TransactionResource(transactionService);
    when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
  }

  @Test
  void addTransactionReturnSavedTransactionDtoWhenAllFieldsValidDedicatedToSimpleOrBorrowOrLoanTransaction() {
    // given
    LocalDateTime timeBefore = LocalDateTime.now();
    User user = UserRandomTool.randomUser();
    Wallet wallet = WalletRandomTool.randomWallet(user);
    Category category = CategoryRandomTool.randomCategory();
    while (category.getType() == Type.BORROW_BACK || category.getType() == Type.LOAN_BACK)
      category.setType(TransactionRandomTool.randomTransactionType());
    TransactionDto transactionDto = TransactionRandomTool.randomTransactionDto();
    // when
    when(walletProvider.get(any(), any())).thenReturn(Optional.of(wallet));
    when(categoryService.getCategory(anyString(), any())).thenReturn(Optional.of(category));
    when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Transaction.class));
    ResponseEntity<TransactionDto> savedTransactionDtoResponeEntity = transactionResource.addTransaction(user::getEmail, wallet.getId(), transactionDto);
    // then
    TransactionDto expectedSavedTransactionDto = TransactionDto.builder().build();
    Assertions.assertEquals(HttpStatus.CREATED, savedTransactionDtoResponeEntity.getStatusCode());
    assertThat(savedTransactionDtoResponeEntity.getBody()).isEqualToIgnoringNullFields(expectedSavedTransactionDto);
    assertThat(savedTransactionDtoResponeEntity.getBody().getDateOfPurchase()).isBetween(timeBefore, LocalDateTime.now());
  }
}
