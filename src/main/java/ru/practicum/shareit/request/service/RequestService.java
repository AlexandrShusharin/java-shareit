package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto add(long userId, ItemRequestDto itemRequestDto);
    ItemRequestDto get(long userId, long requestId);
    List<ItemRequestDto> getAllByOwner(long userId);
    List<ItemRequestDto> getAll(long userId, int from, int size);

}
