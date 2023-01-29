package ru.practicum.shareit.booking.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingValidatorTest {
    @InjectMocks
    BookingValidator bookingValidator;
    @Mock
    BookingRepository bookingRepository;

    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private Item item2;
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

        user3 = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@mail.ru")
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
                .available(false)
                .owner(user1)
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
    }

    @Test
    void validateBookingIsExist() {
        when(bookingRepository.findById(bookingItem1.getId())).thenReturn(Optional.empty());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> bookingValidator.validateBookingIsExist(bookingItem1.getId()));
        assertEquals(exception.getMessage(),
                String.format("Бронирование с id = %s не существует", bookingItem1.getId()));
    }

    @Test
    void validateUserIsItemOwner() {
        final UserNotOwnerException exception = Assertions.assertThrows(
                UserNotOwnerException.class,
                () -> bookingValidator.validateUserIsItemOwner(user2.getId(), bookingItem1));
        assertEquals(exception.getMessage(), "Пользователь не является владельцем вещи");
    }

    @Test
    void validateItemIsAvailable() {
        final ObjectIncorrectArguments exception = Assertions.assertThrows(
                ObjectIncorrectArguments.class,
                () -> bookingValidator.validateItemIsAvailable(item2));
        assertEquals(exception.getMessage(), "Вещь недоступна для бронирования");
    }

    @Test
    void validateUserIsItemOrBookingOwner() {
        final UserNotOwnerException exception = Assertions.assertThrows(
                UserNotOwnerException.class,
                () -> bookingValidator.validateUserIsItemOrBookingOwner(user3.getId(), bookingItem1));
        assertEquals(exception.getMessage(), "Пользователь не является владельцем вещи" +
                " или заявителем бронирования.");
    }


    @Test
    void validateBookingDates() {
        bookingItem1.setEnd(bookingItem1.getStart().minusDays(1));
        final ObjectIncorrectArguments exception = Assertions.assertThrows(
                ObjectIncorrectArguments.class,
                () -> bookingValidator.validateBookingDates(bookingItem1));
        assertEquals(exception.getMessage(), "Время начало бронирования не может быть позже" +
                " времени окончания бронирования");
    }

    @Test
    void validateBookerIsNotOwner() {
        bookingItem1.setBooker(user1);
        final BookerIsItemOwnerException exception = Assertions.assertThrows(
                BookerIsItemOwnerException.class,
                () -> bookingValidator.validateBookerIsNotOwner(bookingItem1, user1.getId()));
        assertEquals(exception.getMessage(), "Пользователь не может забронировать свою вещь");
    }

    @Test
    void validateBookingStatus() {
        final BookingStatusProcessedException exception = Assertions.assertThrows(
                BookingStatusProcessedException.class,
                () -> bookingValidator.validateBookingStatus(bookingItem2));
        assertEquals(exception.getMessage(), "Нельзя изменить статус обработанного бронирования");
    }

    @Test
    void validateUserMadeItemBooking() {
        when(bookingRepository.findBookingsByBooker_IdAndItem_IdAndStatusAndEndBefore(eq(user1.getId()),
                eq(item2.getId()), eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        final UserNotItemBookerException exception = Assertions.assertThrows(
                UserNotItemBookerException.class,
                () -> bookingValidator.validateUserMadeItemBooking(user1.getId(), item2.getId()));
        assertEquals(exception.getMessage(),
                String.format("Пользователь с id = %s не бронировал вещь с id = %s ",
                        user1.getId(), item2.getId()));
    }
}