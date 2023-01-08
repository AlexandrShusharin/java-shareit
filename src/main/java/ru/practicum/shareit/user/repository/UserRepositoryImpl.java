package ru.practicum.shareit.user.repository;
/*
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UserRepositoryImpl implements UserRepository {

    private long lastId = 0;
    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public User get(long id) {
        return users.get(id);
    }

    @Override
    public User add(User user) {
        user.setId(this.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private long getId() {
        return ++lastId;
    }

}
  */