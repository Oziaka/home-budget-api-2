package pl.wallet.transaction.resource;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wallet.transaction.enums.Type;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("${cors.allowed-origins}")
public class TransactionTypeResource {
    @GetMapping("/types")
    public ResponseEntity<List<Type>> getTransactionsType() {
        return ResponseEntity.ok(Arrays.stream(Type.values()).collect(Collectors.toList()));
    }
}
