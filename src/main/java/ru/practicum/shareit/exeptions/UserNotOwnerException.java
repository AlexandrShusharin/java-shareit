package ru.practicum.shareit.exeptions;

public class UserNotOwnerException extends RuntimeException {

    public UserNotOwnerException(String message) {
        super(message);
    }
}
