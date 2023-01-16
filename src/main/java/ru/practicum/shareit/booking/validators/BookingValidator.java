package ru.practicum.shareit.booking.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class BookingValidator {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    public void validateBookingIsExist(long bookingId) {
        if (!bookingRepository.findById(bookingId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Бронирование с id = %s не существует", bookingId));
        }
    }

    public void validateUserIsBookingOwner(long bookerId, Booking booking) {
        if (booking.getBooker().getId() != userRepository.getReferenceById(bookerId).getId()) {
            throw new UserNotOwnerException("Пользователь не является автором бронирования");
        }
    }

    public void validateUserIsItemOwner(long userId, Booking booking) {
        if (booking.getItem().getOwner().getId() != userId) {
            throw new UserNotOwnerException("Пользователь не является владельцем вещи");
        }
    }

    public void validateItemIsAvailable(Item item) {
        if (!item.isAvailable()) {
            throw new ObjectIncorrectArguments("Вещь недоступна для бронирования");
        }
    }

    public void validateUserIsItemOrBookingOwner(long userId, Booking booking) {
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new UserNotOwnerException("Пользователь не является владельцем вещи или заявителем бронирования.");
        }
    }

    public void validateBookingDates (Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ObjectIncorrectArguments("Время начало бронирования не может быть позже" +
                    " времени окончания бронирования");
        }
    }

    public void validateBookerIsNotOwner (Booking booking, long userId) {
        if (booking.getItem().getOwner().getId() == userId) {
            throw new BookerIsItemOwnerException("Пользователь не может забронировать свою вещь");
        }
    }

    public void validateBookingStatus (Booking booking) {
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new BookingStatusProcessedException("Нельзя изменить статус обработанного бронирования");
        }
    }

}
