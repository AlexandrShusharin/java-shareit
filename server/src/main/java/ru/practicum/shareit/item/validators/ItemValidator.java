package ru.practicum.shareit.item.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final ItemRepository itemRepository;

    public void validateItemIsExist(long itemId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Вещь с id = %s не существует", itemId));
        }
    }

    public void validateItemOwner(long userId, long itemId) {
        if (itemRepository.getReferenceById(itemId).getOwner().getId() != userId) {
            throw new ObjectNotFoundException(String.format("Вещь с id = %s не принадлежит пользователю с id = %s",
                    itemId, userId));
        }
    }

}
