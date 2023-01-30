package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDtoRequest {
    private long id;
    @Future(message = "Не верно задано время начало бронирования")
    @NotNull(message = "Не задано время начало бронирования")
    private LocalDateTime start;
    @Future(message = "Не верно задано время окончания бронирования")
    @NotNull(message = "Не задано время окончания бронирования")
    private LocalDateTime end;
    @NotNull(message = "Не задана вещь для бронирования")
    private long itemId;
    private long bookerId;
    private BookingStatus status;
}
