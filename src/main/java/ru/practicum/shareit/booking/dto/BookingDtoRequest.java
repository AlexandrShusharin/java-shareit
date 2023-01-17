package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;


import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDtoRequest {
    long id;
    @Future(message = "Не верно задано время начало бронирования")
    @NotNull(message = "Не задано время начало бронирования")
    LocalDateTime start;
    @Future(message = "Не верно задано время окончания бронирования")
    @NotNull(message = "Не задано время окончания бронирования")
    LocalDateTime end;
    @NotNull(message = "Не задана вещь для бронирования")
    long itemId;
    long bookerId;
    BookingStatus status;
}
