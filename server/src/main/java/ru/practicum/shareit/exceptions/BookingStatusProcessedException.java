package ru.practicum.shareit.exceptions;

public class BookingStatusProcessedException extends RuntimeException {
    public BookingStatusProcessedException(String message) {
        super(message);
    }
}
