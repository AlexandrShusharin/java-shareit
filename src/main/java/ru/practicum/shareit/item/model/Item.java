package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter @ToString
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "available")
    boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;
    @ManyToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;
}
