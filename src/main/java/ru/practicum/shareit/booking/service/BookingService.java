package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {
    BookingDto add (long userId, BookingDto bookingDto);
}
