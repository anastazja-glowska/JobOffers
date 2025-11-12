package pl.joboffers.domain.register;

import org.jetbrains.annotations.NotNull;

class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
