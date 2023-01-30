package ru.practicum.shareit.user.dto;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private long id;
    private String name;
    private String email;
}
