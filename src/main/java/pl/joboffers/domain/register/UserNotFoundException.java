package pl.joboffers.domain.register;

import org.jetbrains.annotations.NotNull;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
