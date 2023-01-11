package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Table(name = "requests")
@Builder
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class ItemRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "requestor_id")
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created;
}
