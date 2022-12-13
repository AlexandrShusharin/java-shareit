package ru.practicum.shareit.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserEmailOccupiedException extends RuntimeException {

    public UserEmailOccupiedException(String message) {
        super(message);
    }
}
