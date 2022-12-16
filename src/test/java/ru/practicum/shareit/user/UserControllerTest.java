package ru.practicum.shareit.user;

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
class UserControllerTest {
    private final MockMvc mockMvc;

    @SneakyThrows
    @Test
    void endpointsTest() {
        String user = "{\"name\":\"user\",\"email\":\"user@user.com\"}";
        mockMvc.perform(
                        post("/users")
                                .contentType("application/json")
                                .content(user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        String user1 = "{\"name\":\"user1\"}";
        mockMvc.perform(
                        patch("/users/1")
                                .contentType("application/json")
                                .content(user1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("user1"));

        mockMvc.perform(
                        get("/users/1")
                                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));


        mockMvc.perform(
                        get("/users")
                                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(
                        delete("/users/1")
                                .contentType("application/json"))
                .andExpect(status().isOk());
    }

}