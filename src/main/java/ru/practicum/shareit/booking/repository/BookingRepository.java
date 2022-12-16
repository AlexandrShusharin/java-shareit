package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository {
    Booking get(long id);

    Booking add(Booking booking);

    Booking update(Booking booking);

    void delete(long id);

    List<Booking> getAll();
}
