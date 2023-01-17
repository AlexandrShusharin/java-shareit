package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoForItem {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    long bookerId;
}
