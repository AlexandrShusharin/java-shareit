package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto add(long userId, ItemRequestDto itemRequestDto);

    ItemRequestResponseDto get(long userId, long requestId);

    List<ItemRequestResponseDto> getAllByOwner(long userId);

    List<ItemRequestResponseDto> getAll(long userId, int from, int size);

}
