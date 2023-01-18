package ru.practicum.shareit.booking.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingValidator {
    private final BookingRepository bookingRepository;

    public void validateBookingIsExist(long bookingId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new ObjectNotFoundException(String.format("Бронирование с id = %s не существует", bookingId));
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

    public void validateBookingDates(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ObjectIncorrectArguments("Время начало бронирования не может быть позже" +
                    " времени окончания бронирования");
        }
    }

    public void validateBookerIsNotOwner(Booking booking, long userId) {
        if (booking.getItem().getOwner().getId() == userId) {
            throw new BookerIsItemOwnerException("Пользователь не может забронировать свою вещь");
        }
    }

    public void validateBookingStatus(Booking booking) {
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new BookingStatusProcessedException("Нельзя изменить статус обработанного бронирования");
        }
    }

    public void validateUserMadeItemBooking(long userId, long itemId) {
        List<Booking> userBookings = new ArrayList<>(bookingRepository
                .findBookingsByBooker_IdAndItem_IdAndStatusAndEndBefore(userId, itemId,
                        BookingStatus.APPROVED, LocalDateTime.now()));
        if (userBookings.size() == 0) {
            throw new UserNotItemBookerException(String.format("Пользователь с id = %s не бронировал вещь с id = %s ",
                    userId, itemId));
        }
    }

}
