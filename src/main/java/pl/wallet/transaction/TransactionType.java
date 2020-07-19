package pl.wallet.transaction;

import pl.wallet.Wallet;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum TransactionType {

    EXPENSE(BigDecimal::subtract, BigDecimal::add),
    REVENUES(BigDecimal::add, BigDecimal::subtract),
    BORROW(BigDecimal::add, BigDecimal::subtract),
    LOAN(BigDecimal::subtract, BigDecimal::add),
    BORROW_BACK(BigDecimal::subtract, BigDecimal::add),
    LOAN_BACK(BigDecimal::add, BigDecimal::subtract);

    private BiFunction<BigDecimal, BigDecimal, BigDecimal> changeBalanceWhenTransactionIsAdding;
    private BiFunction<BigDecimal, BigDecimal, BigDecimal> changeBalanceWhenTransactionIsRemoving;

    TransactionType(BiFunction<BigDecimal, BigDecimal, BigDecimal> changeBalanceWhenTransactionIsAdding,
                    BiFunction<BigDecimal, BigDecimal, BigDecimal> changeBalanceWhenTransactionIsRemoving) {
        this.changeBalanceWhenTransactionIsAdding = changeBalanceWhenTransactionIsAdding;
        this.changeBalanceWhenTransactionIsRemoving = changeBalanceWhenTransactionIsRemoving;
    }

    public BigDecimal countBalance(Wallet wallet, Transaction transaction) {
        return changeBalanceWhenTransactionIsAdding.apply(wallet.getBalance(), transaction.getPrice());
    }

    public BigDecimal undoCountBalance(Wallet wallet, Transaction transaction) {
        return changeBalanceWhenTransactionIsRemoving.apply(wallet.getBalance(), transaction.getPrice());
    }
}
