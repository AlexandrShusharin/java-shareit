package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    private BookingDtoRequest bookingDtoRequest;
    private BookingDtoResponse bookingDtoResponse;
    private Item item;
    private User user;
    private BookingState state;
    private int from;
    private int size;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("good hammer")
                .description("very good hammer")
                .available(true)
                .owner(user)
                .build();

        bookingDtoRequest = BookingDtoRequest.builder()
                .id(1L)
                .itemId(item.getId())
                .bookerId(user.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        from = 0;
        size = 1;
        state = BookingState.CURRENT;
    }

    @SneakyThrows
    @Test
    void addBooking() {
        when(bookingService.add(user.getId(), bookingDtoRequest)).thenReturn(bookingDtoResponse);

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", user.getId())
                                .content(mapper.writeValueAsString(bookingDtoRequest))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoRequest.getItemId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class));
    }

    @SneakyThrows
    @Test
    void updateBooking() {
        when(bookingService.updateStatus(user.getId(), bookingDtoRequest.getItemId(), true))
                .thenReturn(bookingDtoResponse);

        mockMvc.perform(
                        patch("/bookings/{id}", bookingDtoRequest.getItemId())
                                .header("X-Sharer-User-Id", user.getId())
                                .param("approved", "true")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoRequest.getItemId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class));
    }

    @SneakyThrows
    @Test
    void getBookingById() {
        when(bookingService.get(user.getId(), bookingDtoRequest.getItemId()))
                .thenReturn(bookingDtoResponse);

        mockMvc.perform(
                        get("/bookings/{id}", bookingDtoRequest.getItemId())
                                .header("X-Sharer-User-Id", user.getId())
                                .param("approved", "true")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoRequest.getItemId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class));
    }

    @SneakyThrows
    @Test
    void getBookingListByOwner() {
        when(bookingService.getBookingListByOwner(user.getId(), state, from, size))
                .thenReturn(List.of(bookingDtoResponse));

        mockMvc.perform(
                        get("/bookings/owner", bookingDtoRequest.getItemId())
                                .header("X-Sharer-User-Id", user.getId())
                                .param("state", String.valueOf(state))
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Long.class));
    }

    @SneakyThrows
    @Test
    void getBookingListByUser() {
        when(bookingService.getBookingListByUser(user.getId(), state, from, size))
                .thenReturn(List.of(bookingDtoResponse));

        mockMvc.perform(
                        get("/bookings/", bookingDtoRequest.getItemId())
                                .header("X-Sharer-User-Id", user.getId())
                                .param("state", String.valueOf(state))
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Long.class));
    }
}