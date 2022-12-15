package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item get(long id);
    Item add(Item item);
    Item update(Item item);
    void delete(long id);
    List<Item> getAll();
    List<Item> getUserItems(long userId);
}
