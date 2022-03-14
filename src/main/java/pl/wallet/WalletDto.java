package pl.wallet;

import lombok.*;
import pl.user.UserDto;
import pl.wallet.currency.CurrencyDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Set;

@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class WalletDto {

    @Null(message = "Wallet must not have id")
    private Long id;

    @NotEmpty(message = "Wallet must have name")
    private String name;

    @NotNull(message = "Wallet must have balance")
    private BigDecimal balance;

    private Set<UserDto> users;

    private UserDto owner;

    private CurrencyDto currencyDto;

    @Builder
    public WalletDto(@Null(message = "Wallet must not have id") Long id, @NotEmpty(message = "Wallet must have name") String name, @NotNull(message = "Wallet must have balance") BigDecimal balance, Set<UserDto> users, UserDto owner, CurrencyDto currencyDto) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.users = users;
        this.owner = owner;
        this.currencyDto = currencyDto;
    }
}
