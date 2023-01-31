package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    private Long requestId;
}
