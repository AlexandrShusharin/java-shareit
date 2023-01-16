package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {
    BookingDtoResponse add (long userId, BookingDtoRequest bookingDto);
    BookingDtoResponse updateStatus(long userId, long bookingId, boolean approved);
    BookingDtoResponse get(long userId, long bookingId);
    List<BookingDtoResponse> getBookingListByUser(long bookerId, BookingState state);
    List<BookingDtoResponse> getBookingListByOwner(long userId, BookingState state);
}
