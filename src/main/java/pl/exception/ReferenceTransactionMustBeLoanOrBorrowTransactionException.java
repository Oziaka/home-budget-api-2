package pl.exception;

public class ReferenceTransactionMustBeLoanOrBorrowTransactionException extends RuntimeException {

  public ReferenceTransactionMustBeLoanOrBorrowTransactionException () {
    super("Reference transaction must be loan or borrow transaction");
  }
}
