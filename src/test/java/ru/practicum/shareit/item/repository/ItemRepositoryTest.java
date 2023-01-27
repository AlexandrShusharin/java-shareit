package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository requestRepository;
    private Item item;
    private User user1;
    private ItemRequest requestFromUser1;
    private User user2;
    private String text;


    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();

        requestFromUser1 = ItemRequest.builder()
                .description("need hummer")
                .created(LocalDateTime.now())
                .requestor(user1)
                .build();

        user2 = User.builder()
                .name("user2")
                .email("user2@mail.ru")
                .build();

        item = Item.builder()
                .name("good hammer")
                .description("very good hammer")
                .available(true)
                .request(requestFromUser1)
                .owner(user2)
                .build();

        text = "%hAm%";
    }

    @Test
    void findItemsByOwner_Id() {
        user1 = userRepository.save(user1);
        requestFromUser1.setRequestor(user1);
        requestFromUser1 = requestRepository.save(requestFromUser1);
        item.setRequest(requestFromUser1);
        user2 = userRepository.save(user2);
        item.setOwner(user2);
        item = itemRepository.save(item);
        List<Item> foundItems = itemRepository.findItemsByOwner_Id(user2.getId(), Pageable.unpaged());
        assertThat(foundItems, hasSize(1));
        assertThat(foundItems.get(0).getOwner().getId(), is(user2.getId()));
    }

    @Test
    void findItemsByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableTrue() {
        user1 = userRepository.save(user1);
        requestFromUser1.setRequestor(user1);
        requestFromUser1 = requestRepository.save(requestFromUser1);
        item.setRequest(requestFromUser1);
        user2 = userRepository.save(user2);
        item.setOwner(user2);
        item = itemRepository.save(item);
        List<Item> foundItems = itemRepository.
                findItemsByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableTrue(text, text,
                        Pageable.unpaged());
        assertThat(foundItems, hasSize(1));
    }

    @Test
    void findAllByRequest_Id() {
        user1 = userRepository.save(user1);
        requestFromUser1.setRequestor(user1);
        requestFromUser1 = requestRepository.save(requestFromUser1);
        item.setRequest(requestFromUser1);
        user2 = userRepository.save(user2);
        item.setOwner(user2);
        item = itemRepository.save(item);
        List<Item> foundItems = itemRepository.findAllByRequest_Id(requestFromUser1.getId());
        assertThat(foundItems, hasSize(1));
        assertThat(foundItems.get(0).getRequest().getId(), is(requestFromUser1.getId()));
    }
}