package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
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
        bookingValidator.validateBookerIsNotOwner(booking, userId);
        return BookingMapper.bookingToDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoResponse updateStatus(long userId, long bookingId, boolean approved) {
        userValidator.validateUserIsExist(userId);
        bookingValidator.validateBookingIsExist(bookingId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        bookingValidator.validateUserIsItemOwner(userId, booking);
        bookingValidator.validateBookingStatus(booking);
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
    public List<BookingDtoResponse> getBookingListByOwner(long userId, BookingState state) {
        userValidator.validateUserIsExist(userId);
        List<BookingDtoResponse> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByOwnerAll(userId)
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookings = bookingRepository.findByOwnerAndPast(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());

                break;
            case FUTURE:
                bookings = bookingRepository.findByUserAndFuture(userId,LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookings = bookingRepository.findByOwnerAndCurrent(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookings = bookingRepository.findByOwnerAndByStatus(userId, BookingStatus.WAITING)
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookings = bookingRepository.findByOwnerAndByStatus(userId, BookingStatus.REJECTED)
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
        }
        return bookings;
    }

    @Override
    public List<BookingDtoResponse> getBookingListByUser(long bookerId, BookingState state) {
        userValidator.validateUserIsExist(bookerId);
        List<BookingDtoResponse> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findBookingByBooker_Id(bookerId, Sort.by("id").descending())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookings = bookingRepository.findBookingByBooker_IdAndEndIsBefore(bookerId, LocalDateTime.now(),
                                Sort.by("id").descending())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookings = bookingRepository.findBookingByBooker_IdAndStartIsAfter(bookerId, LocalDateTime.now(),
                                Sort.by("id").descending())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookings = bookingRepository.findBookingByBooker_IdAndStartIsBeforeAndEndIsAfter(bookerId,
                                LocalDateTime.now(), LocalDateTime.now(), Sort.by("id").descending())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookings = bookingRepository.findBookingByBooker_IdAndStatus(bookerId, BookingStatus.WAITING,
                                Sort.by("id").descending())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookings = bookingRepository.findBookingByBooker_IdAndStatus(bookerId, BookingStatus.REJECTED,
                                Sort.by("id").descending())
                        .stream()
                        .map(BookingMapper::bookingToDtoResponse)
                        .collect(Collectors.toList());
                break;
        }
        return bookings;
    }
}
