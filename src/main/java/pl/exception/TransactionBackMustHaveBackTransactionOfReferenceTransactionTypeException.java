package pl.exception;

public class TransactionBackMustHaveBackTransactionOfReferenceTransactionTypeException extends RuntimeException {
   public TransactionBackMustHaveBackTransactionOfReferenceTransactionTypeException() {
      super("When you wont to reference to loan transaction your back transaction must be loan back and for borrow must have borrow back");
   }
}
