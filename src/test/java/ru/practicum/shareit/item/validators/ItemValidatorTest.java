package ru.practicum.shareit.item.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemValidatorTest {
    @InjectMocks
    ItemValidator itemValidator;
    @Mock
    private ItemRepository itemRepository;
    private User user1;
    private Item item1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@mail.ru")
                .build();

        user2 = User.builder()
                .id(2L)
                .name("user2")
                .email("user2@mail.ru")
                .build();

        item1 = Item.builder()
                .id(1L)
                .name("good hammer1")
                .description("very good hammer2")
                .available(true)
                .owner(user1)
                .build();
    }
    @Test
    void validateItemIsExist() {
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.empty());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> itemValidator.validateItemIsExist(item1.getId()));
        assertEquals(exception.getMessage(),
                String.format("Вещь с id = %s не существует", item1.getId()));
    }

    @Test
    void validateItemOwner() {
        item1.setOwner(user2);
        when(itemRepository.getReferenceById(item1.getId())).thenReturn(item1);
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> itemValidator.validateItemOwner(item1.getId(), user1.getId()));
        assertEquals(exception.getMessage(),
                String.format("Вещь с id = %s не принадлежит пользователю с id = %s", item1.getId(), user1.getId()));
    }
}