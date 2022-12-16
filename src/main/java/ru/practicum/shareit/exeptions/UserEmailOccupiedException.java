package ru.practicum.shareit.exeptions;

public class UserEmailOccupiedException extends RuntimeException {

    public UserEmailOccupiedException(String message) {
        super(message);
    }
}
