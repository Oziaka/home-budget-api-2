package pl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class ThereIsNoYourPropertyException extends RuntimeException {
   public ThereIsNoYourPropertyException() {
      super("There is no your property");
   }
}

