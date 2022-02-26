package pl.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AppRuntimeException extends RuntimeException {
    private AppError appError;

    public AppRuntimeException(AppError appError) {
        super(appError.getMessage());
        this.appError = appError;
    }

    public String getMessage() {
        return appError.getMessage();
    }

    public HttpStatus getStatus() {
        return appError.getStatus();
    }
}