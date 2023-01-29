package ru.practicum.shareit.exceptions;

public class UserNotItemBookerException extends RuntimeException {
    public UserNotItemBookerException(String message) {
        super(message);
    }
}
