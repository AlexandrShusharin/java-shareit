package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody BookingDtoRequest bookingDto) {
        log.info("POST-запрос по адресу /bookings/, userId=" + userId + ", тело запроса:" + bookingDto);
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                            @RequestParam boolean approved) {
        log.info("PATCH-запрос по адресу /bookings/, userId=" + userId + ", bookingId:" + id +
                ", approved=" + approved);
        return bookingClient.updateBookings(userId, id, approved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long id) {
        log.info("GET-запрос по адресу /bookings/, userId=" + userId + ", bookingId:" + id);
        return bookingClient.getBooking(userId, id);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingListByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @RequestParam(defaultValue = "ALL") BookingState state,
                                                          @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                                          @RequestParam(defaultValue = "1000000") @Min(value = 1)  int size) {
        log.info("GET-запрос по адресу /bookings/owner, userId=" + userId + ", state=" + state);
        return bookingClient.getBookingListByOwner(userId, state, from, size);
    }

    @GetMapping()
    public ResponseEntity<Object> getBookingListByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(defaultValue = "ALL") BookingState state,
                                                         @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                                         @RequestParam(defaultValue = "1000000") @Min(value = 1) int size) {
        log.info("GET-запрос по адресу /bookings/, userId=" + userId + ", state=" + state + ", from=" + from +
                ", size=" + size);
        return bookingClient.getBookingListByUser(userId, state, from, size);
    }



    /*
    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

 */
}
