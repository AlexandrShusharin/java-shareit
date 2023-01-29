package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("GЕT-запрос по адресу /items/" + id + ", userId=" + userId);
        return itemService.get(id, userId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                      @RequestParam(defaultValue = "1000000") @Min(value = 1) int size) {

        log.info("GЕT-запрос по адресу /items/, userId=" + userId);
        return itemService.getUserItems(userId, from, size);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("DELETE-запрос по адресу /items/" + id + ", userId=" + userId);
        itemService.delete(userId, id);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("POST-запрос по адресу /items/, userId=" + userId + ", тело запроса:" + itemDto);
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                              @RequestBody ItemDto itemDto) {
        log.info("PATCH-запрос по адресу /items/, userId=" + userId + ", тело запроса:" + itemDto);
        return itemService.update(userId, id, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> findItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text,
                                   @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                   @RequestParam(defaultValue = "1000000") @Min(value = 1) int size) {
        log.info("GET-запрос по адресу /items/search?text=" + text + ", userId=" + userId);
        return itemService.findItems(text, from, size);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("POST-запрос по адресу /items/" + id + "/comments, userId=" + userId + ", тело запроса:" + commentDto);
        return itemService.addComment(userId, id, commentDto);
    }

}
