package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.item.id IN" +
            " (SELECT i.id FROM Item i WHERE i.owner.id = ?1)" +
            " ORDER BY b.id DESC")
    List<Booking> findByOwnerAll(long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN " +
            "(SELECT i.id FROM Item i WHERE i.owner.id = ?1) AND b.status = ?2" +
            " ORDER BY b.id DESC")
    List<Booking> findByOwnerAndByStatus(long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN " +
            "(SELECT i.id FROM Item i WHERE i.owner.id = ?1) AND b.start < ?2 AND b.end > ?2" +
            " ORDER BY b.id DESC")
    List<Booking> findByOwnerAndCurrent(long userId, LocalDateTime currentDate);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN " +
            "(SELECT i.id FROM Item i WHERE i.owner.id = ?1) AND b.end < ?2" +
            " ORDER BY b.id DESC")
    List<Booking> findByOwnerAndPast(long userId, LocalDateTime currentDate);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN " +
            "(SELECT i.id FROM Item i WHERE i.owner.id = ?1) AND b.start > ?2" +
            " ORDER BY b.id DESC")
    List<Booking> findByUserAndFuture(long userId, LocalDateTime currentDate);

    List<Booking> findBookingByBooker_Id(long bookerId, Sort sort);

    List<Booking> findBookingByBooker_IdAndStatus(long bookerId, BookingStatus status, Sort sort);

    List<Booking> findBookingByBooker_IdAndStartIsAfter(long bookerId, LocalDateTime currentTime, Sort sort);

    List<Booking> findBookingByBooker_IdAndEndIsBefore(long bookerId, LocalDateTime currentTime, Sort sort);

    List<Booking> findBookingByBooker_IdAndStartIsBeforeAndEndIsAfter(long bookerId, LocalDateTime currentTime,
                                                                      LocalDateTime anotherCurrentTime, Sort sort);

    Booking findFirst1BookingByItem_IdAndStartIsBefore(long itemId, LocalDateTime currentTime, Sort sort);

    Booking findFirst1BookingByItem_IdAndStartIsAfter(long itemId, LocalDateTime currentTime, Sort sort);

    List<Booking> findBookingsByBooker_IdAndItem_IdAndStatusAndEndBefore(long bookerId,
                                                                         long itemId,
                                                                         BookingStatus status,
                                                                         LocalDateTime currentTime);
}
