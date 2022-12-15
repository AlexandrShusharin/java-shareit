package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validators.ItemValidator;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;

    @Override
    public ItemDto add(long userId, ItemDto ItemDto) {
        userValidator.validateUserIsExist(userId);
        Item item = ItemMapper.toItem(ItemDto);
        item.setOwner(userRepository.get(userId));
        return ItemMapper.toItemDto(itemRepository.add(item));
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto ItemDto) {
        userValidator.validateUserIsExist(userId);
        itemValidator.validateItemIsExist(itemId);
        itemValidator.validateItemOwner(userId, itemId);
        Item existItem = itemRepository.get(itemId);
        if (ItemDto.getName() != null) {
            existItem.setName(ItemDto.getName());
        }
        if (ItemDto.getDescription() != null) {
            existItem.setDescription(ItemDto.getDescription());
        }
        if (ItemDto.getAvailable() != null) {
            existItem.setAvailable(ItemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.update(existItem));
    }

    @Override
    public void delete(long userId, long itemId) {
        userValidator.validateUserIsExist(userId);
        itemValidator.validateItemIsExist(itemId);
        itemValidator.validateItemOwner(userId, itemId);
        itemRepository.delete(itemId);
    }

    @Override
    public ItemDto get(long itemId) {
        itemValidator.validateItemIsExist(itemId);
        return ItemMapper.toItemDto(itemRepository.get(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        return new ArrayList<>(itemRepository.getUserItems(userId).stream()
                .map(o -> ItemMapper.toItemDto(o))
                .collect(Collectors.toList()));
    }

    @Override
    public List<ItemDto> findItems(String text) {
        if (text.length() > 0) {
            return new ArrayList<>(itemRepository.getAll().stream()
                    .filter(o -> (o.getName().toLowerCase().contains(text.toLowerCase()) ||
                            o.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                            o.isAvailable())
                    .map(o -> ItemMapper.toItemDto(o))
                    .collect(Collectors.toList()));
        } else {
            return new ArrayList<>();
        }

    }
}
