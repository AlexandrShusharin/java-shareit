package ru.practicum.shareit.item.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Primary
@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();
    private long lastId = 0;

    @Override
    public Item get(long id) {
        return items.get(id);
    }

    @Override
    public Item add(Item item) {
        item.setId(this.getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void delete(long id) {
        items.remove(id);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return new ArrayList<>(items.values().stream()
                .filter(o -> o.getOwner().getId() == userId)
                .collect(Collectors.toList()));
    }

    private long getId() {
        return ++lastId;
    }
}
