package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    long id;
    @NotNull(message = "Не задано время начало бронирования")
    LocalDateTime start;
    @NotNull(message = "Не задано время окончания бронирования")
    LocalDateTime end;
    @NotNull(message = "Не задана вещь для бронирования")
    long itemId;
    @NotNull(message = "Не задан пользователь бронирования")
    long bookerId;
    BookingStatus status;
}
