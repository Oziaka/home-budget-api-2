package pl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id, Class clazz) {
        super("Could not found " + clazz.getSimpleName() + " : " + id);
    }

    public EntityNotFoundException(String name, Class clazz) {
        super("Could not found " + clazz.getSimpleName() + " : " + name);
    }
}
