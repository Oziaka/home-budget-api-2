package pl.wallet.transaction.enums;

import pl.wallet.Wallet;
import pl.wallet.transaction.model.Transaction;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum Type {

    EXPENSE(BigDecimal::subtract, BigDecimal::add),
    REVENUES(BigDecimal::add, BigDecimal::subtract),
    BORROW(BigDecimal::add, BigDecimal::subtract),
    LOAN(BigDecimal::subtract, BigDecimal::add),
    BORROW_BACK(BigDecimal::subtract, BigDecimal::add),
    LOAN_BACK(BigDecimal::add, BigDecimal::subtract);

    private BiFunction<BigDecimal, BigDecimal, BigDecimal> changeBalanceWhenTransactionIsAdding;
    private BiFunction<BigDecimal, BigDecimal, BigDecimal> changeBalanceWhenTransactionIsRemoving;

    Type(BiFunction<BigDecimal, BigDecimal, BigDecimal> changeBalanceWhenTransactionIsAdding,
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
