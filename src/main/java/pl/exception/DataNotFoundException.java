package pl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class DataNotFoundException extends RuntimeException {
   public DataNotFoundException(String message) {
      super(message);
   }
}
