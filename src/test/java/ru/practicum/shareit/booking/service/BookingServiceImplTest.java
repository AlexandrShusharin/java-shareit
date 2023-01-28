package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validators.BookingValidator;
import ru.practicum.shareit.exceptions.ObjectIncorrectArguments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validators.ItemValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private ItemValidator itemValidator;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingValidator bookingValidator;

    private int from;
    private int size;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private BookingDtoRequest bookingDtoRequestItem1;
    private Booking bookingItem1;
    private Booking bookingItem2;

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

        item2 = Item.builder()
                .id(2L)
                .name("good hammer2")
                .description("very good hammer2")
                .available(true)
                .owner(user1)
                .build();

        bookingDtoRequestItem1 = BookingDtoRequest.builder()
                .id(1L)
                .itemId(item1.getId())
                .bookerId(user2.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();


        bookingItem1 = Booking.builder()
                .id(1L)
                .item(item1)
                .booker(user2)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .build();

        bookingItem2 = Booking.builder()
                .id(2L)
                .item(item2)
                .booker(user1)
                .status(BookingStatus.REJECTED)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .build();

        from = 0;
        size = 1;
    }

    @Test
    void add() {
        doNothing().when(userValidator).validateUserIsExist(user2.getId());
        doNothing().when(itemValidator).validateItemIsExist(bookingDtoRequestItem1.getItemId());

        when(itemRepository.getReferenceById(bookingDtoRequestItem1.getItemId())).thenReturn(item1);
        when(userRepository.getReferenceById(user2.getId())).thenReturn(user2);

        doNothing().when(bookingValidator).validateBookingDates(any(Booking.class));
        doNothing().when(bookingValidator).validateItemIsAvailable(item1);
        doNothing().when(bookingValidator).validateBookerIsNotOwner(any(Booking.class), eq(user2.getId()));

        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingItem1);

        BookingDtoResponse bookingResponse = bookingService.add(user2.getId(), bookingDtoRequestItem1);
        assertThat(bookingResponse.getBooker().getId(), is(user2.getId()));
        assertThat(bookingResponse.getItem().getId(), is(item1.getId()));
        assertThat(bookingResponse.getItem().getId(), is(bookingDtoRequestItem1.getItemId()));
        assertThat(bookingResponse.getStatus(), is(BookingStatus.WAITING));
    }

    @Test
    void addWrongDate() {
        doNothing().when(userValidator).validateUserIsExist(user2.getId());
        doNothing().when(itemValidator).validateItemIsExist(bookingDtoRequestItem1.getItemId());
        when(itemRepository.getReferenceById(bookingDtoRequestItem1.getItemId())).thenReturn(item1);
        when(userRepository.getReferenceById(user2.getId())).thenReturn(user2);
        doThrow(new ObjectIncorrectArguments("invalid data")).when(bookingValidator).validateBookingDates(any(Booking.class));

        final ObjectIncorrectArguments exception = Assertions.assertThrows(
                ObjectIncorrectArguments.class,
                () -> bookingService.add(user2.getId(), bookingDtoRequestItem1));
        assertEquals("invalid data", exception.getMessage());
    }

    @Test
    void addItemNotAvailable() {
        doNothing().when(userValidator).validateUserIsExist(user2.getId());
        doNothing().when(itemValidator).validateItemIsExist(bookingDtoRequestItem1.getItemId());
        when(itemRepository.getReferenceById(bookingDtoRequestItem1.getItemId())).thenReturn(item1);
        when(userRepository.getReferenceById(user2.getId())).thenReturn(user2);
        doThrow(new ObjectIncorrectArguments("unavailable")).when(bookingValidator).validateItemIsAvailable(item1);

        final ObjectIncorrectArguments exception = Assertions.assertThrows(
                ObjectIncorrectArguments.class,
                () -> bookingService.add(user2.getId(), bookingDtoRequestItem1));
        assertEquals("unavailable", exception.getMessage());
    }


    @Test
    void updateStatus() {
        doNothing().when(userValidator).validateUserIsExist(user1.getId());
        doNothing().when(bookingValidator).validateBookingIsExist(bookingDtoRequestItem1.getItemId());
        when(bookingRepository.getReferenceById(bookingDtoRequestItem1.getItemId())).thenReturn(bookingItem1);
        doNothing().when(bookingValidator).validateUserIsItemOwner(user1.getId(), bookingItem1);
        doNothing().when(bookingValidator).validateBookingStatus(bookingItem1);
        when(bookingRepository.save(bookingItem1)).thenReturn(bookingItem1);

        BookingDtoResponse bookingResponse = bookingService.updateStatus(user1.getId(),
                bookingDtoRequestItem1.getItemId(), true);
        assertThat(bookingResponse.getId(), is(bookingDtoRequestItem1.getId()));
        assertThat(bookingResponse.getStatus(), is(BookingStatus.APPROVED));

        bookingResponse = bookingService.updateStatus(user1.getId(),
                bookingDtoRequestItem1.getItemId(), false);
        assertThat(bookingResponse.getId(), is(bookingDtoRequestItem1.getId()));
        assertThat(bookingResponse.getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void get() {
        doNothing().when(userValidator).validateUserIsExist(user1.getId());
        doNothing().when(bookingValidator).validateBookingIsExist(bookingDtoRequestItem1.getItemId());
        when(bookingRepository.getReferenceById(bookingDtoRequestItem1.getItemId())).thenReturn(bookingItem1);
        doNothing().when(bookingValidator).validateUserIsItemOrBookingOwner(user1.getId(), bookingItem1);

        BookingDtoResponse bookingResponse = bookingService.get(user1.getId(), bookingDtoRequestItem1.getItemId());
        assertThat(bookingResponse.getId(), is(bookingDtoRequestItem1.getId()));
    }

    @Test
    void getBookingListByOwner() {
        doNothing().when(userValidator).validateUserIsExist(user1.getId());
        when(bookingRepository.findByOwnerAll(anyLong(), any())).thenReturn(List.of(bookingItem1, bookingItem2));
        when(bookingRepository.findByOwnerAndByStatus(anyLong(), eq(BookingStatus.WAITING), any()))
                .thenReturn(List.of(bookingItem1));
        when(bookingRepository.findByOwnerAndByStatus(anyLong(), eq(BookingStatus.REJECTED), any()))
                .thenReturn(List.of(bookingItem2));

        List<BookingDtoResponse> bookings = bookingService.getBookingListByOwner(user1.getId(), BookingState.ALL,
                from, size);
        assertThat(bookings, hasSize(2));

        bookings = bookingService.getBookingListByOwner(user1.getId(), BookingState.WAITING,
                from, size);
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0).getStatus(), is(BookingStatus.WAITING));

        bookings = bookingService.getBookingListByOwner(user1.getId(), BookingState.REJECTED,
                from, size);
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0).getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void getBookingListByUser() {
        doNothing().when(userValidator).validateUserIsExist(user1.getId());
        when(bookingRepository.findBookingByBooker_Id(anyLong(), any())).thenReturn(List.of(bookingItem1, bookingItem2));
        when(bookingRepository.findBookingByBooker_IdAndStatus(anyLong(), eq(BookingStatus.WAITING), any()))
                .thenReturn(List.of(bookingItem1));
        when(bookingRepository.findBookingByBooker_IdAndStatus(anyLong(), eq(BookingStatus.REJECTED), any()))
                .thenReturn(List.of(bookingItem2));

        List<BookingDtoResponse> bookings = bookingService.getBookingListByUser(user1.getId(), BookingState.ALL,
                from, size);
        assertThat(bookings, hasSize(2));

        bookings = bookingService.getBookingListByUser(user1.getId(), BookingState.WAITING,
                from, size);
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0).getStatus(), is(BookingStatus.WAITING));

        bookings = bookingService.getBookingListByUser(user1.getId(), BookingState.REJECTED,
                from, size);
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0).getStatus(), is(BookingStatus.REJECTED));
    }
}