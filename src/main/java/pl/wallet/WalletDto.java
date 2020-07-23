package pl.wallet;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.user.UserDto;
import pl.wallet.transaction.dto.TransactionDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
@EqualsAndHashCode
public class WalletDto {

    private Long id;

    @NotEmpty(message = "Wallet must have name")
    private String name;

    @NotNull(message = "Wallet must have balance")
    private BigDecimal balance;

    private List<TransactionDto> transactions;

    private List<UserDto> users;

    private UserDto owner;

    @Builder
    public WalletDto(Long id, @NotEmpty(message = "Wallet must have name") String name, @NotNull(message = "Wallet must have balance") BigDecimal balance, List<TransactionDto> transactions, List<UserDto> users, UserDto owner) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.transactions = transactions;
        this.users = users;
        this.owner = owner;
    }
}
