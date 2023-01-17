package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "text")
    String text;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @JoinColumn(name = "created")
    LocalDateTime created;
}
