package pl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidTransactionException extends RuntimeException {
  public InvalidTransactionException() {
    super("Invalid transaction");
  }
}
