package pl.user.item_key;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserItemKeyResourceTest {

   private UserItemKeyResource userItemKeyResource;
   private UserItemKeyService userItemKeyService;
   private UserItemKeyRepository userItemKeyRepository;

   @BeforeEach
   void init() {
      userItemKeyRepository = Mockito.mock(UserItemKeyRepository.class);
      userItemKeyService = new UserItemKeyService(userItemKeyRepository);
      userItemKeyResource = new UserItemKeyResource(userItemKeyService);
   }

   @Test
   void addUserItemKeyReturnUserItemKeyDto() {
      // given
      UserItemKey userItemKey = UserItemKeyRandomTool.randomUserItemKey();// when
      Long id = 1L;
      when(userItemKeyRepository.save(any())).thenAnswer(invocation -> {
         UserItemKey argument = invocation.getArgument(0, UserItemKey.class);
         argument.setId(id);
         return argument;
      });
      ResponseEntity<UserItemKeyDto> userItemKeyDtoResponseEntity = userItemKeyResource.addUserItemKey(UserItemKeyMapper.toDto(userItemKey));
      // then
      userItemKey.setId(id);
      UserItemKeyDto expectedUserItemKey = UserItemKeyMapper.toDto(userItemKey);
      Assertions.assertEquals(HttpStatus.OK, userItemKeyDtoResponseEntity.getStatusCode());
      Assertions.assertEquals(expectedUserItemKey, userItemKeyDtoResponseEntity.getBody());

   }
}