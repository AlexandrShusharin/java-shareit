package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("GЕT-запрос по адресу /users");
        return userService.getUsers();
    }

    @GetMapping("/{id}") //получить пользователя по id
    public UserDto getUserById(@PathVariable long id) {
        log.info("GET-запрос по адресу /users/" + id);
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("DELETE-запрос по адресу /users/" + id);
        userService.delete(id);
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("POST-запрос по адресу /users тело запроса: " + userDto);
        return userService.add(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        log.info("PATCH-запрос по адресу /users/" + id + " тело запроса: " + userDto);
        return userService.update(id, userDto);
    }

}
