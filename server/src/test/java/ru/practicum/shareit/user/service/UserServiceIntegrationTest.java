package ru.practicum.shareit.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceIntegrationTest {
    final UserService userService;
    final ObjectMapper mapper;
    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        userDto1 = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@mail.ru")
                .build();

        userDto2 = UserDto.builder()
                .id(2L)
                .name("user2")
                .email("user2@mail.ru")
                .build();
    }

    @Test
    void getUsersTest() {
        userService.add(userDto1);
        userService.add(userDto2);
        assertThat(userService.getUsers(), hasSize(2));
    }
}
