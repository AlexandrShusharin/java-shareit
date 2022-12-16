package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
class ItemControllerTest {
    private final MockMvc mockMvc;

    @SneakyThrows
    @Test
    void endpointsItemControllerTest() {
        String user = "{\"name\":\"user\",\"email\":\"user@user.com\"}";
        mockMvc.perform(
                        post("/users")
                                .contentType("application/json")
                                .content(user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        String item = "{\"name\":\"Дрель\",\"description\":\"Простая дрель\",\"available\": true}";
        mockMvc.perform(
                        post("/items")
                                .contentType("application/json")
                                .header("X-Sharer-User-Id", 1)
                                .content(item))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        String item1 = "{\"name\":\"Дрель1\"}";
        mockMvc.perform(
                        patch("/items/1")
                                .contentType("application/json")
                                .header("X-Sharer-User-Id", 1)
                                .content(item1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Дрель1"));

        mockMvc.perform(
                        get("/items/1")
                                .contentType("application/json")
                                .header("X-Sharer-User-Id", 1)
                                .content(item))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        mockMvc.perform(
                        get("/items/")
                                .contentType("application/json")
                                .header("X-Sharer-User-Id", 1)
                                .content(item))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(
                        get("/items/?search=дрел")
                                .contentType("application/json")
                                .header("X-Sharer-User-Id", 1)
                                .content(item))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(
                        delete("/items/1")
                                .contentType("application/json")
                                .header("X-Sharer-User-Id", 1)
                                .content(item))
                .andExpect(status().isOk());
    }
}