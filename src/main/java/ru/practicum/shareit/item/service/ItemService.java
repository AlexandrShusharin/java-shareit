package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {
    ItemDto add(long userId, ItemDto itemDto);

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    void delete(long userId, long itemId);

    ItemDtoWithBooking get(long itemId);

    List<ItemDtoWithBooking> getUserItems(long userId);

    List<ItemDtoWithBooking> findItems(String text);
}
