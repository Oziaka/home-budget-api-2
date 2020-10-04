package pl.wallet.transaction.resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.wallet.transaction.enums.Type;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTypeResourceTest {
   private TransactionTypeResource transactionTypeResource = new TransactionTypeResource();

   @Test
   void getTransactionsTypeReturnAllTransactionTypes() {
      // given

      // when
      ResponseEntity<List<Type>> transactionsTypeResponseEntity = transactionTypeResource.getTransactionsType();
      // then
      List<Type> expectedTransactionTypes = Arrays.stream(Type.values()).collect(Collectors.toList());
      Assertions.assertEquals(HttpStatus.OK, transactionsTypeResponseEntity.getStatusCode());
      assertEquals(expectedTransactionTypes, transactionsTypeResponseEntity.getBody());
   }
}