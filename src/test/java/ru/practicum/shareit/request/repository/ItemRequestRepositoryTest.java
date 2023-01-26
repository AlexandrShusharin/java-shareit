package ru.practicum.shareit.request.repository;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
@AutoConfigureTestDatabase
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    private ItemRequest requestFromUser1;
    private ItemRequest requestFromUser2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();

        user2 = User.builder()
                .name("user2")
                .email("user2@mail.ru")
                .build();


        requestFromUser1 = ItemRequest.builder()
                .description("need hummer")
                .created(LocalDateTime.now())
                .requestor(user1)
                .build();

        requestFromUser2 = ItemRequest.builder()
                .description("need сar")
                .created(LocalDateTime.now())
                .requestor(user2)
                .build();
    }

    @Transactional
    @Test
    void findAllByRequestor_Id() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        requestFromUser1.setRequestor(user1);
        requestFromUser2.setRequestor(user2);

        requestRepository.save(requestFromUser1);
        requestRepository.save(requestFromUser2);

        List<ItemRequest>  requests= requestRepository.findAllByRequestor_Id(user1.getId());
        assertThat(requests, hasSize(1));
        assertThat(requests.get(0).getRequestor().getId(), is(user1.getId()));
    }

    @Transactional
    @Test
    void findAllByRequestor_IdNot() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        requestFromUser1.setRequestor(user1);
        requestFromUser2.setRequestor(user2);

        requestRepository.save(requestFromUser1);
        requestRepository.save(requestFromUser2);

        List<ItemRequest>  requests= requestRepository.findAllByRequestor_IdNot(user1.getId(), Pageable.unpaged());
        assertThat(requests, hasSize(1));
        assertThat(requests.get(0).getRequestor().getId(), Matchers.not(user1.getId()));
    }
}