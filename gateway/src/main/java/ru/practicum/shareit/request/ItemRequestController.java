package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestClient requestClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("GЕT-запрос по адресу /requests/" + id + ", userId=" + userId);
        return requestClient.getRequest(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GЕT-запрос по адресу /requests/ userId=" + userId);
        return requestClient.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                               @RequestParam(defaultValue = "1000000") @Min(value = 1) int size) {
        log.info("GET-запрос по адресу /requests/all?from=" + from + "&size=" + size + ", userId=" + userId);
        return requestClient.getAllRequests(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST-запрос по адресу /requests/, userId=" + userId + ", тело запроса:" + itemRequestDto);
        return requestClient.addRequest(userId, itemRequestDto);
    }
}
