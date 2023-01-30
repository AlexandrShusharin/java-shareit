package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.User;

@Data
@Builder
public class ItemForItemRequestDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private long requestId;

}
