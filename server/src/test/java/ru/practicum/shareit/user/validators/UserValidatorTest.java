package ru.practicum.shareit.user.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.UserEmailOccupiedException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {
    @InjectMocks
    UserValidator userValidator;
    @Mock
    UserRepository userRepository;
    private User user1;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@mail.ru")
                .build();
    }

    @Test
    void validateEmailNotOccupied() {
        when(userRepository.findAll()).thenReturn(List.of(user1));
        final UserEmailOccupiedException exception = Assertions.assertThrows(
                UserEmailOccupiedException.class,
                () -> userValidator.validateEmailNotOccupied(user1.getEmail()));
        assertEquals(exception.getMessage(),
                String.format(String.format("Email = %s уже занят", user1.getEmail())));
    }

    @Test
    void validateUserIsExist() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.empty());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> userValidator.validateUserIsExist(user1.getId()));
        assertEquals(exception.getMessage(),
                String.format("Пользователь с id = %s не существует", user1.getId()));
    }
}