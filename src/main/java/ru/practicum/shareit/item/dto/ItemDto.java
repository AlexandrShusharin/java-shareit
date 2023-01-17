package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    long id;
    @NotBlank(message = "Не задано название вещи")
    String name;
    @NotBlank(message = "Не задано описание вещи")
    String description;
    @NotNull(message = "Не задана доступность вещи")
    Boolean available;
    BookingDtoForItem lastBooking;
    BookingDtoForItem nextBooking;
    List<CommentDto> comments;
    Long request;
}
