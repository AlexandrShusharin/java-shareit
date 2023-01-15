package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validators.BookingValidator;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validators.ItemValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.util.List;

@Primary
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingValidator bookingValidator;

    @Override
    public BookingDtoResponse add(long userId, BookingDtoRequest bookingDto) {
        userValidator.validateUserIsExist(userId);
        itemValidator.validateItemIsExist(bookingDto.getItemId());
        Booking booking = BookingMapper.bookingFromDto(bookingDto,
                itemRepository.getReferenceById(bookingDto.getItemId()), userRepository.getReferenceById(userId));
        booking.setStatus(BookingStatus.WAITING);
        bookingValidator.validateItemIsAvailable(booking.getItem());
        bookingValidator.validateBookingDates(booking);
        return BookingMapper.bookingToDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoResponse updateStatus(long userId, long bookingId, boolean approved) {
        userValidator.validateUserIsExist(userId);
        bookingValidator.validateBookingIsExist(bookingId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        bookingValidator.validateUserIsItemOwner(userId, booking);
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.bookingToDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoResponse get(long userId, long bookingId) {
        userValidator.validateUserIsExist(userId);
        bookingValidator.validateBookingIsExist(bookingId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        bookingValidator.validateUserIsItemOrBookingOwner(userId, booking);
        return BookingMapper.bookingToDtoResponse(booking);
    }

    @Override
    public List<BookingDtoResponse> getBookingListByUser(User user, BookingState state) {
        return null;
    }

    @Override
    public List<BookingDtoResponse> getBookingListByOwner(User user, BookingState state) {
        return null;
    }
}
