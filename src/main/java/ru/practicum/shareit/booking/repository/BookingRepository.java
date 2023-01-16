package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.item.id IN" +
            " (SELECT i.id FROM Item i WHERE i.owner.id = ?1)" +
            " ORDER BY b.id DESC")
    List<Booking> findByUserAll(long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN " +
            "(SELECT i.id FROM Item i WHERE i.owner.id = ?1) AND b.status = ?2" +
            " ORDER BY b.id DESC")
    List<Booking> findByUserAndByStatus(long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN " +
            "(SELECT i.id FROM Item i WHERE i.owner.id = ?1) AND b.status = ?2 AND b.start < ?3 AND b.end > ?3" +
            " ORDER BY b.id DESC")
    List<Booking> findByUserAndCurrent(long userId, BookingStatus status, LocalDateTime currentDate);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN " +
            "(SELECT i.id FROM Item i WHERE i.owner.id = ?1) AND b.status = ?2 AND b.end < ?3" +
            " ORDER BY b.id DESC")
    List<Booking> findByUserAndPast(long userId, BookingStatus status, LocalDateTime currentDate);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN " +
            "(SELECT i.id FROM Item i WHERE i.owner.id = ?1) AND b.status = ?2 AND b.start > ?3" +
            " ORDER BY b.id DESC")
    List<Booking> findByUserAndFuture(long userId, BookingStatus status, LocalDateTime currentDate);

//    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 ORDER BY b.id DESC")
//    List<Booking> findByBookerAll(long bookerId);

    List<Booking> findBookingByBooker_Id(long bookerId, Sort sort);

//    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 and b.status = ?2 ORDER BY b.id DESC")
    List<Booking> findBookingByBooker_IdAndStatus(long bookerId, BookingStatus status, Sort sort);
    List<Booking> findBookingByBooker_IdAndStartIsAfter(long bookerId, LocalDateTime currentTime, Sort sort);
    List<Booking> findBookingByBooker_IdAndEndIsBefore(long bookerId, LocalDateTime currentTime, Sort sort);
    List<Booking> findBookingByBooker_IdAndStartIsBeforeAndEndIsAfter(long bookerId, LocalDateTime currentTime,
                                                                      LocalDateTime anotherCurrentTime, Sort sort);
    }
