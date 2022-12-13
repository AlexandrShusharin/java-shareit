package ru.practicum.shareit.booking.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Primary
@Component
@AllArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {
    private final HashMap<Long, Booking> bookings = new HashMap<>();

    @Override
    public Booking get(long id) {
       return bookings.get(id);
    }

    @Override
    public Booking add(Booking booking) {
        booking.setId(this.getId());
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Booking update(Booking booking) {
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public void delete(long id) {
        bookings.remove(id);
    }

    @Override
    public List<Booking> getAll() {
        return new ArrayList<>(bookings.values());
    }

    private long getId() {
        long lastId = bookings.entrySet().stream()
                .mapToLong(o -> o.getValue().getId())
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
