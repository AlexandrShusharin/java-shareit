package ru.practicum.shareit.user.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @InjectMocks
    private UserServiceImpl userService;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();

        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();
    }

    @SneakyThrows
    @Test
    void add() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto addedUserDto = userService.add(userDto);
        assertThat(addedUserDto, equalTo(userDto));
    }

    @Test
    void update() {
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(userRepository.getReferenceById(userDto.getId()))
                .thenReturn(user);
        doNothing().when(userValidator).validateUserIsExist(userDto.getId());
        UserDto addedUserDto = userService.update(userDto.getId(), userDto);
        assertThat(addedUserDto, equalTo(userDto));
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void delete() {
    }

    @Test
    void get() {
    }

    @Test
    void getUsers() {
    }
}