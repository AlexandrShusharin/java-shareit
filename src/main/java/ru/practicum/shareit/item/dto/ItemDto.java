package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    Long request;
}
