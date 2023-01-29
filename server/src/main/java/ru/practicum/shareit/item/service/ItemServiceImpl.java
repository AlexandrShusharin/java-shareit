package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validators.BookingValidator;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validators.ItemValidator;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.validators.RequestValidator;
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
    private final BookingValidator bookingValidator;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final RequestValidator requestValidator;

    @Override
    public ItemDto add(long userId, ItemDto itemDto) {
        userValidator.validateUserIsExist(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.getReferenceById(userId));
        if (itemDto.getRequestId() != null) {
            requestValidator.validateRequestIsExist(itemDto.getRequestId());
            item.setRequest(itemRequestRepository.getReferenceById(itemDto.getRequestId()));
        }
        return toItemDtoWithBooking(itemRepository.save(item));
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
        if (itemDto.getRequestId() != null) {
            requestValidator.validateRequestIsExist(itemDto.getRequestId());
            existItem.setRequest(itemRequestRepository.getReferenceById(itemDto.getRequestId()));
        }
        return toItemDtoWithBooking(itemRepository.save(existItem));
    }

    @Override
    public void delete(long userId, long itemId) {
        userValidator.validateUserIsExist(userId);
        itemValidator.validateItemIsExist(itemId);
        itemValidator.validateItemOwner(userId, itemId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDto get(long itemId, long userId) {
        itemValidator.validateItemIsExist(itemId);
        if (itemRepository.getReferenceById(itemId).getOwner().getId() == userId) {
            return this.toItemDtoWithBooking(itemRepository.getReferenceById(itemId));
        } else {
            return this.toItemDtoWithBookingForNotOwner(itemRepository.getReferenceById(itemId));
        }
    }

    @Override
    public List<ItemDto> getUserItems(long userId, int from, int size) {
        Sort sortById = Sort.by("id").ascending();
        Pageable page = PageRequest.of(getPageNumber(from, size), size, sortById);
        return new ArrayList<>(itemRepository.findItemsByOwner_Id(userId, page)
                .stream()
                .map(this::toItemDtoWithBooking)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ItemDto> findItems(String text, int from, int size) {
        Sort sortById = Sort.by("id").ascending();
        Pageable page = PageRequest.of(getPageNumber(from, size), size, sortById);
        String searchText = "%" + text + "%";
        if (text.length() > 0) {
            return new ArrayList<>(
                    itemRepository.findItemsByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableTrue(
                                    searchText, searchText, page).stream()
                            .map(this::toItemDtoWithBooking)
                            .collect(Collectors.toList()));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        itemValidator.validateItemIsExist(itemId);
        userValidator.validateUserIsExist(userId);
        bookingValidator.validateUserMadeItemBooking(userId, itemId);
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(itemRepository.getReferenceById(itemId));
        comment.setAuthor(userRepository.getReferenceById(userId));
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private ItemDto toItemDtoWithBooking(Item item) {
        Booking lastBooking = bookingRepository.findFirst1BookingByItem_IdAndStartIsBefore(item.getId(),
                LocalDateTime.now(), Sort.by("start").descending());
        Booking nextBooking = bookingRepository.findFirst1BookingByItem_IdAndStartIsAfter(item.getId(),
                LocalDateTime.now(), Sort.by("start").ascending());

        BookingDtoForItem lastBookingDto = lastBooking != null ? BookingMapper.bookingToDtoForItem(lastBooking) : null;
        BookingDtoForItem nextBookingDto = nextBooking != null ? BookingMapper.bookingToDtoForItem(nextBooking) : null;

        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setLastBooking(lastBookingDto);
        itemDto.setNextBooking(nextBookingDto);
        itemDto.setComments(getItemComments(item.getId()));

        return itemDto;
    }

    private ItemDto toItemDtoWithBookingForNotOwner(Item item) {
        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setComments(getItemComments(item.getId()));
        return itemDto;
    }

    private List<CommentDto> getItemComments(long itemId) {
        return commentRepository.findCommentsByItem_Id(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private int getPageNumber(int from, int size) {
        return from / size;
    }
}
