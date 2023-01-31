package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("GЕT-запрос по адресу /items/" + id + ", userId=" + userId);
        return itemClient.getItem(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                               @RequestParam(defaultValue = "1000000") @Min(value = 1) int size) {
        log.info("GЕT-запрос по адресу /items/, userId=" + userId);
        return itemClient.getUserItems(userId, from, size);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("DELETE-запрос по адресу /items/" + id + ", userId=" + userId);
        itemClient.deleteItem(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("POST-запрос по адресу /items/, userId=" + userId + ", тело запроса:" + itemDto);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                             @RequestBody ItemDto itemDto) {
        log.info("PATCH-запрос по адресу /items/, userId=" + userId + ", тело запроса:" + itemDto);
        return itemClient.updateItem(userId, id, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text,
                                            @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                            @RequestParam(defaultValue = "1000000") @Min(value = 1) int size) {
        log.info("GET-запрос по адресу /items/search?text=" + text + ", userId=" + userId);
        return itemClient.findItems(userId, text, from, size);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("POST-запрос по адресу /items/" + id + "/comments, userId=" + userId + ", тело запроса:" + commentDto);
        return itemClient.addComment(userId, id, commentDto);
    }

}
