package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequestDto {
    private long id;
    @NotBlank(message = "Не задано описание запроса")
    private String description;
    private User requestor;
    private LocalDateTime created;
}
