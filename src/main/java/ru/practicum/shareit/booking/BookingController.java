package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST-запрос по адресу /items/, userId=" + userId + ", тело запроса:" + bookingDto);
        return bookingService.add(userId, bookingDto);
    }
}
