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
    private long id;
    @NotBlank(message = "Не задано название вещи")
    private String name;
    @NotBlank(message = "Не задано описание вещи")
    private String description;
    @NotNull(message = "Не задана доступность вещи")
    private Boolean available;
    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
