package ru.practicum.shareit.user.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void updateInvalidUserId() {
        doThrow(new ObjectNotFoundException("404")).when(userValidator).validateUserIsExist(userDto.getId());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () ->userService.update(userDto.getId(), userDto));
        Assertions.assertEquals("404", exception.getMessage());
    }

    @Test
    void delete() {
        doNothing().when(userRepository).deleteById(userDto.getId());
        userService.delete(userDto.getId());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }

    @Test
    void get() {
        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        doNothing().when(userValidator).validateUserIsExist(userDto.getId());
        UserDto existUserDto = userService.get(user.getId());
        assertThat(existUserDto, equalTo(userDto));
    }

    @Test
    void getUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDto> existUsersDto = userService.getUsers();
        assertThat(existUsersDto, hasSize(1));
    }
}