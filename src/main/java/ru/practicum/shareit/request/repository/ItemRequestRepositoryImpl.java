package ru.practicum.shareit.request.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Primary
@Component
public class ItemRequestRepositoryImpl implements ItemRequestRepository {
    private final HashMap<Long, ItemRequest> itemRequests = new HashMap<>();

    @Override
    public ItemRequest get(long id) {
        return itemRequests.get(id);
    }

    @Override
    public ItemRequest add(ItemRequest itemRequest) {
        itemRequest.setId(this.getId());
        itemRequests.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest update(ItemRequest itemRequest) {
        itemRequests.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public void delete(long id) {
        itemRequests.remove(id);
    }

    @Override
    public List<ItemRequest> getAll() {
        return new ArrayList<>(itemRequests.values());
    }

    private long getId() {
        long lastId = itemRequests.entrySet().stream()
                .mapToLong(o -> o.getValue().getId())
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
