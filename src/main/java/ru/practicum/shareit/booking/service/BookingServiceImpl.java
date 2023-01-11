package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validators.ItemValidator;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

@Primary
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto add(long userId, BookingDto bookingDto) {
        userValidator.validateUserIsExist(userId);
        itemValidator.validateItemIsExist(bookingDto.getItemId());
        Booking booking = BookingMapper.bookingFromDto(bookingDto,
                itemRepository.getReferenceById(bookingDto.getId()), userRepository.getReferenceById(userId));
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.bookingToDto(bookingRepository.save(booking));
    }
}
