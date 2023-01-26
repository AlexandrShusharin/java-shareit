package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    private RequestService requestService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    private ItemRequestDto requestDto;
    private ItemRequestDto requestResponseDto;
    private ItemRequestResponseDto requestResponseDtoex;
    private long userId;
    private int from;
    private int size;
    private long requestId;


    @BeforeEach
    void setUp() {
        userId = 1;
        requestId = 1;
        from = 0;
        size = 1;

        requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Need hammer")
                .build();

        requestResponseDto = ItemRequestDto.builder()
                .id(1L)
                .requestor(new User(1L, "user", "user@mail.ru"))
                .created(LocalDateTime.now())
                .description("Need hammer")
                .build();

        requestResponseDtoex = ItemRequestResponseDto.builder()
                .id(1L)
                .requestor(new User(1L, "user", "user@mail.ru"))
                .created(LocalDateTime.now())
                .description("Need hammer")
                .items(new ArrayList<ItemForItemRequestDto>())
                .build();
    }

    @SneakyThrows
    @Test
    void getRequestById() {
        when(requestService.get(userId, 1L))
                .thenReturn(requestResponseDtoex);
        mockMvc.perform(
                        get("/requests/{requestId}", requestId)
                                .header("X-Sharer-User-Id", userId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @SneakyThrows
    @Test
    void getOwnRequests() {
        when(requestService.getAllByOwner(userId))
                .thenReturn(List.of(requestResponseDtoex));
        mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", userId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @SneakyThrows
    @Test
    void findRequests() {
        when(requestService.getAll(userId, from, size))
                .thenReturn(List.of(requestResponseDtoex));
        mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", userId)
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @SneakyThrows
    @Test
    void findRequestsWrongParamFrom() {
        from = -1;
        mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", userId)
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void findRequestsWrongParamSize() {
        size = -1;
        mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", userId)
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addRequest() {
        when(requestService.add(userId, requestDto))
                .thenReturn(requestResponseDto);
        mockMvc.perform(
                        post("/requests")
                                .content(mapper.writeValueAsString(requestDto))
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestResponseDto.getId()))
                .andExpect(jsonPath("$.description").value(requestResponseDto.getDescription()))
                .andExpect(jsonPath("$.requestor.id").value(requestResponseDto.getRequestor().getId()));
    }

    @SneakyThrows
    @Test
    void addRequestWhitEmptyDescription() {
        when(requestService.add(userId, requestDto))
                .thenReturn(requestResponseDto);
        requestDto.setDescription("");
        mockMvc.perform(
                        post("/requests")
                                .content(mapper.writeValueAsString(requestDto))
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}