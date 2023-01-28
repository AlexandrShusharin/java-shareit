package ru.practicum.shareit.exceptions;

public class UserEmailOccupiedException extends RuntimeException {

    public UserEmailOccupiedException(String message) {
        super(message);
    }
}
