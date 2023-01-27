package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validators.BookingValidator;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validators.ItemValidator;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.validators.RequestValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private ItemValidator itemValidator;
    @Mock
    private BookingValidator bookingValidator;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private RequestValidator requestValidator;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private User user;
    private User userBooker;
    private Item item;
    private ItemRequest request;
    private Comment comment;
    private Booking booking;
    private int from;
    private int size;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();

        userBooker = User.builder()
                .id(2L)
                .name("user1")
                .email("user1@mail.ru")
                .build();

        request = ItemRequest.builder()
                .id(1L)
                .description("need hummer")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();

        item = Item.builder()
                .id(1L)
                .name("good hammer")
                .description("very good hammer")
                .available(true)
                .request(request)
                .owner(user)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("good hammer")
                .description("very good hammer")
                .available(true)
                .requestId(request.getId())
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .text("nice hammer")
                .authorName(user.getName())
                .build();

        comment = Comment.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .text("nice hammer")
                .author(user)
                .item(item)
                .build();

        booking = Booking.builder()
                .id(1L)
                .item(item)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(2))
                .booker(userBooker)
                .build();

        from = 0;
        size = 1;
    }

    @Test
    void add() {
        doNothing().when(userValidator).validateUserIsExist(user.getId());
        doNothing().when(requestValidator).validateRequestIsExist(itemDto.getRequestId());

        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        when(requestRepository.getReferenceById(itemDto.getRequestId())).thenReturn(request);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto addedItemDto = itemService.add(user.getId(), itemDto);
        assertThat(addedItemDto.getName(), is(itemDto.getName()));
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void addWrongUser() {
        doThrow(new ObjectNotFoundException("404")).when(userValidator).validateUserIsExist(user.getId());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () ->  itemService.add(user.getId(), itemDto));
        assertEquals("404", exception.getMessage());
    }

    @Test
    void addWrongRequest() {
        doThrow(new ObjectNotFoundException("404")).when(requestValidator).validateRequestIsExist(user.getId());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () ->  itemService.add(user.getId(), itemDto));
        assertEquals("404", exception.getMessage());
    }


    @Test
    void update() {
        doNothing().when(userValidator).validateUserIsExist(user.getId());
        doNothing().when(requestValidator).validateRequestIsExist(itemDto.getRequestId());
        doNothing().when(itemValidator).validateItemOwner(user.getId(), itemDto.getId());
        doNothing().when(itemValidator).validateItemIsExist(itemDto.getId());

        when(itemRepository.getReferenceById(itemDto.getId())).thenReturn(item);
        when(requestRepository.getReferenceById(itemDto.getRequestId())).thenReturn(request);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto addedItemDto = itemService.update(user.getId(), itemDto.getId(), itemDto);
        assertThat(addedItemDto.getName(), is(itemDto.getName()));
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void updateWrongItem() {
        doThrow(new ObjectNotFoundException("404")).when(itemValidator).validateItemIsExist(item.getId());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () ->  itemService.update(user.getId(), itemDto.getId(), itemDto));
        assertEquals("404", exception.getMessage());
    }

    @Test
    void updateUserNotOwner() {
        doThrow(new ObjectNotFoundException("404")).when(itemValidator).validateItemOwner(user.getId(),item.getId());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () ->  itemService.update(user.getId(), itemDto.getId(), itemDto));
        assertEquals("404", exception.getMessage());
    }

    @Test
    void delete() {
        doNothing().when(userValidator).validateUserIsExist(user.getId());
        doNothing().when(itemValidator).validateItemOwner(user.getId(), itemDto.getId());
        doNothing().when(itemValidator).validateItemIsExist(itemDto.getId());
        doNothing().when(itemRepository).deleteById(item.getId());
        itemService.delete(user.getId(), item.getId());
        verify(itemRepository, times(1)).deleteById(any());
    }

    @Test
    void get() {
        doNothing().when(itemValidator).validateItemIsExist(itemDto.getId());
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(bookingRepository.findFirst1BookingByItem_IdAndStartIsBefore(anyLong(), any(), any())).thenReturn(booking);
        when(bookingRepository.findFirst1BookingByItem_IdAndStartIsAfter(anyLong(), any(), any())).thenReturn(booking);
        when(commentRepository.findCommentsByItem_Id(anyLong())).thenReturn((List.of(comment)));
        ItemDto gotItemDto = itemService.get(itemDto.getId(), user.getId());
        assertThat(gotItemDto.getName(), is(itemDto.getName()));

        verify(bookingRepository, times(1)).findFirst1BookingByItem_IdAndStartIsBefore(anyLong(),
                any(), any());
        verify(commentRepository, times(1)).findCommentsByItem_Id(anyLong());
    }

    @Test
    void getForNotOwner() {
        doNothing().when(itemValidator).validateItemIsExist(itemDto.getId());
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(commentRepository.findCommentsByItem_Id(anyLong())).thenReturn((List.of(comment)));

        ItemDto gotItemDto = itemService.get(itemDto.getId(), userBooker.getId());

        assertThat(gotItemDto.getName(), is(itemDto.getName()));
        verify(bookingRepository, times(0)).findFirst1BookingByItem_IdAndStartIsBefore(anyLong(),
                any(), any());
        verify(commentRepository, times(1)).findCommentsByItem_Id(anyLong());
    }

    @Test
    void getUserItems() {
        when(itemRepository.findItemsByOwner_Id(
                eq(user.getId()), any())).thenReturn(List.of(item));
        List<ItemDto> items = itemService.getUserItems(user.getId(),  from, size);
        assertThat(items, hasSize(1));
    }

    @Test
    void findItems() {
        when(itemRepository.findItemsByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableTrue(
                anyString(), anyString(), any())).thenReturn(List.of(item));
        List<ItemDto> items = itemService.findItems("ham",  from, size);
        assertThat(items, hasSize(1));
    }

    @Test
    void findItemsWithEmptyText() {
        List<ItemDto> items = itemService.findItems("",  from, size);
        assertThat(items, hasSize(0));
        verify(itemRepository, times(0))
                .findItemsByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableTrue(any(),any(),
                        any());
    }

    @Test
    void addComment() {
        doNothing().when(userValidator).validateUserIsExist(user.getId());
        doNothing().when(itemValidator).validateItemIsExist(itemDto.getId());
        doNothing().when(bookingValidator).validateUserMadeItemBooking(user.getId(), itemDto.getId());

        when(itemRepository.getReferenceById(itemDto.getId())).thenReturn(item);
        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto addedCommentDto = itemService.addComment(user.getId(), itemDto.getId(), commentDto);
        assertThat(addedCommentDto.getText(), is(commentDto.getText()));
    }

}