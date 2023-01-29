package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@AutoConfigureTestDatabase
class CommentRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository requestRepository;
    @Autowired
    private CommentRepository commentRepository;
    private User user1;
    private User user2;
    private Item item;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();
        user1 = userRepository.save(user1);

        ItemRequest requestFromUser1 = ItemRequest.builder()
                .description("need hummer")
                .created(LocalDateTime.now())
                .requestor(user1)
                .build();
        requestFromUser1 = requestRepository.save(requestFromUser1);

        user2 = User.builder()
                .name("user2")
                .email("user2@mail.ru")
                .build();

        user2 = userRepository.save(user2);

        item = Item.builder()
                .name("good hammer")
                .description("very good hammer")
                .available(true)
                .request(requestFromUser1)
                .owner(user2)
                .build();
        item = itemRepository.save(item);

        Comment comment = Comment.builder()
                .text("goood")
                .created(LocalDateTime.now())
                .author(user1)
                .item(item)
                .build();
        commentRepository.save(comment);
    }

    @Test
    void findCommentsByItem_Id() {
        List<Comment> commnets = commentRepository.findCommentsByItem_Id(item.getId());
        assertThat(commnets, hasSize(1));
        assertThat(commnets.get(0).getText(), is("goood"));
    }
}