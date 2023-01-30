package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestBody BookingDtoRequest bookingDto) {
        log.info("POST-запрос по адресу /bookings/, userId=" + userId + ", тело запроса:" + bookingDto);
        return bookingService.add(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    public BookingDtoResponse updateBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                            @RequestParam boolean approved) {
        log.info("PATCH-запрос по адресу /bookings/, userId=" + userId + ", bookingId:" + id +
                ", approved=" + approved);
        return bookingService.updateStatus(userId, id, approved);
    }

    @GetMapping("/{id}")
    public BookingDtoResponse getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long id) {
        log.info("GET-запрос по адресу /bookings/, userId=" + userId + ", bookingId:" + id);
        return bookingService.get(userId, id);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingListByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @RequestParam(defaultValue = "ALL") BookingState state,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "1000000") int size) {
        log.info("GET-запрос по адресу /bookings/owner, userId=" + userId + ", state=" + state);
        return bookingService.getBookingListByOwner(userId, state, from, size);
    }

    @GetMapping()
    public List<BookingDtoResponse> getBookingListByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(defaultValue = "ALL") BookingState state,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "1000000") int size) {
        log.info("GET-запрос по адресу /bookings/, userId=" + userId + ", state=" + state + ", from=" + from +
                ", size=" + size);
        return bookingService.getBookingListByUser(userId, state, from, size);
    }

}
