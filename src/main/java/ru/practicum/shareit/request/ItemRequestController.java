package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestService requestService;
    @GetMapping("/{id}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("GЕT-запрос по адресу /requests/" + id + ", userId=" + userId);
        return requestService.get(userId, id);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GЕT-запрос по адресу /requests/ userId=" + userId);
        return requestService.getAllByOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Valid @RequestParam(required = false) @Min(value = 1)  int from,
                                          @Valid @RequestParam(required = false) @Min(value = 1) int size) {
        log.info("GET-запрос по адресу /requests/all?from=" + from + "&size=" + size + ", userId=" + userId);
        return requestService.getAll(userId, from, size);
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST-запрос по адресу /requests/, userId=" + userId + ", тело запроса:" + itemRequestDto);
        return requestService.add(userId, itemRequestDto);
    }
}
