package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    private ItemDto itemDto;
    private CommentDto commentDto;

    private User user;
    private int from;
    private int size;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("good hammer")
                .description("very good hammer")
                .available(true)
                .requestId(user.getId())
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .text("nice hammer")
                .authorName(user.getName())
                .build();

        from = 0;
        size = 1;
    }

    @SneakyThrows
    @Test
    void getItemById() {
        when(itemService.get(user.getId(), itemDto.getId())).thenReturn(itemDto);
        mockMvc.perform(
                        get("/items/{itemId}",itemDto.getId())
                                .header("X-Sharer-User-Id", user.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
        ;
    }

    @SneakyThrows
    @Test
    void getUserItems() {
        when(itemService.getUserItems(user.getId(), from, size)).thenReturn(List.of(itemDto));
        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", user.getId())
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
        ;
    }

    @SneakyThrows
    @Test
    void deleteItem() {
       doNothing().when(itemService).delete(user.getId(), itemDto.getId());
        mockMvc.perform(
                        delete("/items/{id}", itemDto.getId())
                                .header("X-Sharer-User-Id", user.getId())
                )
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void addItem() {
        when(itemService.add(user.getId(), itemDto)).thenReturn(itemDto);
        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", user.getId())
                                .content(mapper.writeValueAsString(itemDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
        ;
    }

    @SneakyThrows
    @Test
    void addItemWithoutName() {
        itemDto.setName("");
        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", user.getId())
                                .content(mapper.writeValueAsString(itemDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        ;
    }

    @SneakyThrows
    @Test
    void addItemWithoutDescription() {
        itemDto.setDescription("");
        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", user.getId())
                                .content(mapper.writeValueAsString(itemDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        ;
    }

    @SneakyThrows
    @Test
    void addItemWithoutAvailable() {
        itemDto.setAvailable(null);
        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", user.getId())
                                .content(mapper.writeValueAsString(itemDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        ;
    }

    @SneakyThrows
    @Test
    void updateItem() {
        when(itemService.update(user.getId(), itemDto.getId(), itemDto)).thenReturn(itemDto);
        mockMvc.perform(
                        patch("/items/{id}", itemDto.getId())
                                .header("X-Sharer-User-Id", user.getId())
                                .content(mapper.writeValueAsString(itemDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
        ;
    }

    @SneakyThrows
    @Test
    void findItems() {
        when(itemService.findItems(itemDto.getName(), from, size)).thenReturn(List.of(itemDto));
        mockMvc.perform(
                        get("/items/search")
                                .header("X-Sharer-User-Id", user.getId())
                                .param("text", itemDto.getName())
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @SneakyThrows
    @Test
    void addComment() {
        when(itemService.addComment(user.getId(), itemDto.getId(), commentDto)).thenReturn(commentDto);
        mockMvc.perform(
                        post("/items/{id}/comment", itemDto.getId())
                                .header("X-Sharer-User-Id", user.getId())
                                .content(mapper.writeValueAsString(commentDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
        ;
    }

    @SneakyThrows
    @Test
    void addCommentWithoutText() {
        when(itemService.addComment(user.getId(), itemDto.getId(), commentDto)).thenReturn(commentDto);
        commentDto.setText("");
        mockMvc.perform(
                        post("/items/{id}/comment", itemDto.getId())
                                .header("X-Sharer-User-Id", user.getId())
                                .content(mapper.writeValueAsString(commentDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }
}