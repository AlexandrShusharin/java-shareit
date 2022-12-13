package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User get(long id);
    User add(User user);
    User update(User user);
    void delete(long id);
    List<User> getAll();
}
