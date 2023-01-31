package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(long userId, ItemDto itemDto);

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    void delete(long userId, long itemId);

    ItemDto get(long itemId, long userId);

    List<ItemDto> getUserItems(long userId, int from, int size);

    List<ItemDto> findItems(String text, int from, int size);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}
