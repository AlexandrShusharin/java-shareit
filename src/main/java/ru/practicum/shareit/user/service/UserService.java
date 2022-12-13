package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);
    UserDto update(long userId, UserDto userDto);
    void delete(long id);
    UserDto get(long id);
    List<UserDto> getUsers();
}
