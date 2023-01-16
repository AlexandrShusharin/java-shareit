package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validators.ItemValidator;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;

    @Override
    public ItemDto add(long userId, ItemDto itemDto) {
        userValidator.validateUserIsExist(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.getReferenceById(userId));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        userValidator.validateUserIsExist(userId);
        itemValidator.validateItemIsExist(itemId);
        itemValidator.validateItemOwner(userId, itemId);
        Item existItem = itemRepository.getReferenceById(itemId);
        if (itemDto.getName() != null) {
            existItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(existItem));
    }

    @Override
    public void delete(long userId, long itemId) {
        userValidator.validateUserIsExist(userId);
        itemValidator.validateItemIsExist(itemId);
        itemValidator.validateItemOwner(userId, itemId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDtoWithBooking get(long itemId) {
        itemValidator.validateItemIsExist(itemId);
        return this.toItemDtoWhitBooking(itemRepository.getReferenceById(itemId));
    }

    @Override
    public List<ItemDtoWithBooking> getUserItems(long userId) {
        return new ArrayList<>(itemRepository.findAll().stream()
                .filter(o -> o.getOwner().getId() == userId)
                .map(this::toItemDtoWhitBooking)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ItemDtoWithBooking> findItems(String text) {
        if (text.length() > 0) {
            return new ArrayList<>(itemRepository.findAll().stream()
                    .filter(o -> (o.getName().toLowerCase().contains(text.toLowerCase()) ||
                            o.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                            o.isAvailable())
                    .map(this::toItemDtoWhitBooking)
                    .collect(Collectors.toList()));
        } else {
            return new ArrayList<>();
        }
    }

    private ItemDtoWithBooking toItemDtoWhitBooking(Item item) {
        Booking lastBooking = bookingRepository.findFirst1BookingByItem_IdAndStartIsBefore(item.getId(),
                LocalDateTime.now(), Sort.by("start").descending());
        Booking nextBooking = bookingRepository.findFirst1BookingByItem_IdAndStartIsAfter(item.getId(),
                LocalDateTime.now(), Sort.by("start").ascending());
        BookingDtoForItem lastBookingDto = lastBooking != null ? BookingMapper.bookingToDtoForItem(lastBooking) : null;
        BookingDtoForItem nextBookingDto = nextBooking != null ? BookingMapper.bookingToDtoForItem(nextBooking) : null;

        return ItemDtoWithBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .lastBooking(lastBookingDto)
                .nextBooking(nextBookingDto)
                .build();
    }
}
