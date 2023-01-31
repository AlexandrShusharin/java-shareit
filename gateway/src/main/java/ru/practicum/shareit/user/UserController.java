package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("GЕT-запрос по адресу /users");
        return userClient.getUsers();
    }

    @GetMapping("/{id}") //получить пользователя по id
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        log.info("GET-запрос по адресу /users/" + id);
        return userClient.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        log.info("DELETE-запрос по адресу /users/" + id);
        return userClient.deleteUser(id);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST-запрос по адресу /users тело запроса: " + userDto);
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        log.info("PATCH-запрос по адресу /users/" + id + " тело запроса: " + userDto);
        return userClient.updateUser(id, userDto);
    }

}
