package pl.exception;

import org.springframework.http.HttpStatus;

public interface AppError {

    HttpStatus getStatus();

    String getMessage();

}