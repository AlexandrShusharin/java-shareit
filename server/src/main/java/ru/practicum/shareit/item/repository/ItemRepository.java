package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByOwner_Id(long ownerId, Pageable page);

    List<Item> findItemsByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableTrue(String text, String text1,
                                                                                        Pageable page);

    List<Item> findAllByRequest_Id(long id);
}
