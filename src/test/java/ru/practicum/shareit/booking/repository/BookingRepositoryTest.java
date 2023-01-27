package ru.practicum.shareit.booking.repository;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
@AutoConfigureTestDatabase
class BookingRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Booking bookingItem1User2;
    private Booking bookingItem2User1;
    LocalDateTime startBooking;
    LocalDateTime endBooking;


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

        item1 = Item.builder()
                .name("good hammer1")
                .description("very good hammer2")
                .available(true)
                .owner(user1)
                .build();

        item2 = Item.builder()
                .name("good hammer2")
                .description("very good hammer2")
                .available(true)
                .owner(user1)
                .build();

        startBooking = LocalDateTime.now().plusDays(1);
        endBooking = LocalDateTime.now().plusDays(3);

        bookingItem1User2 = Booking.builder()
                .booker(user2)
                .item(item1)
                .status(BookingStatus.WAITING)
                .start(startBooking)
                .end(endBooking)
                .build();

        bookingItem2User1 = Booking.builder()
                .booker(user1)
                .item(item2)
                .status(BookingStatus.WAITING)
                .start(startBooking)
                .end(endBooking)
                .build();
    }

    void setData() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        item1.setOwner(user1);
        item2.setOwner(user2);
        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
        bookingItem1User2.setItem(item1);
        bookingItem1User2.setBooker(user2);
        bookingItem2User1.setItem(item2);
        bookingItem2User1.setBooker(user1);
        bookingItem1User2 = bookingRepository.save(bookingItem1User2);
        bookingItem2User1 = bookingRepository.save(bookingItem2User1);
    }

    @Test
    void findByOwnerAll() {
        setData();
        List<Booking> bookingList = bookingRepository.findByOwnerAll(user1.getId(), Pageable.unpaged());
        assertThat( bookingList, hasSize(1));
        assertThat(bookingList.get(0).getItem().getOwner().getId(), is(user1.getId()));
    }

    @Test
    void findByOwnerAndByStatus() {
        setData();
        List<Booking> bookingList = bookingRepository.findByOwnerAndByStatus(user1.getId(), BookingStatus.WAITING,
                Pageable.unpaged());
        assertThat( bookingList, hasSize(1));
        assertThat(bookingList.get(0).getItem().getOwner().getId(), is(user1.getId()));

        bookingList = bookingRepository.findByOwnerAndByStatus(user1.getId(), BookingStatus.APPROVED,
                Pageable.unpaged());
        assertThat(bookingList, hasSize(0));
    }

    @Test
    void findByOwnerAndCurrent() {
        setData();
        List<Booking> bookingList = bookingRepository.findByOwnerAndCurrent(user1.getId(),
               startBooking.plusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(1));
        assertThat(bookingList.get(0).getItem().getOwner().getId(), is(user1.getId()));

        bookingList = bookingRepository.findByOwnerAndCurrent(user1.getId(),
                endBooking.plusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(0));
    }

    @Test
    void findByOwnerAndPast() {
        setData();
        List<Booking> bookingList = bookingRepository.findByOwnerAndPast(user1.getId(),
                endBooking.plusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(1));
        assertThat(bookingList.get(0).getItem().getOwner().getId(), is(user1.getId()));

        bookingList = bookingRepository.findByOwnerAndPast(user1.getId(),
                endBooking.minusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(0));
    }

    @Test
    void findByUserAndFuture() {
        setData();
        List<Booking> bookingList = bookingRepository.findByUserAndFuture(user1.getId(),
                startBooking.minusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(1));
        assertThat(bookingList.get(0).getItem().getOwner().getId(), is(user1.getId()));

        bookingList = bookingRepository.findByUserAndFuture(user1.getId(),
                startBooking.plusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(0));
    }

    @Test
    void findBookingByBooker_Id() {
        setData();
        List<Booking> bookingList = bookingRepository.findBookingByBooker_Id(user1.getId(), Pageable.unpaged());
        assertThat(bookingList, hasSize(1));
        assertThat(bookingList.get(0).getBooker().getId(), is(user1.getId()));
    }

    @Test
    void findBookingByBooker_IdAndStatus() {
        setData();
        List<Booking> bookingList = bookingRepository.findBookingByBooker_IdAndStatus(user1.getId(),
                BookingStatus.WAITING, Pageable.unpaged());
        assertThat( bookingList, hasSize(1));
        assertThat(bookingList.get(0).getBooker().getId(), is(user1.getId()));

        bookingList = bookingRepository.findBookingByBooker_IdAndStatus(user1.getId(), BookingStatus.APPROVED,
                Pageable.unpaged());
        assertThat(bookingList, hasSize(0));
    }

    @Test
    void findBookingByBooker_IdAndStartIsAfter() {
        setData();
        List<Booking> bookingList = bookingRepository.findBookingByBooker_IdAndStartIsAfter(user1.getId(),
                startBooking.minusDays(1), Pageable.unpaged());
        assertThat( bookingList, hasSize(1));
        assertThat(bookingList.get(0).getBooker().getId(), is(user1.getId()));

        bookingList = bookingRepository.findBookingByBooker_IdAndStartIsAfter(user1.getId(),
                startBooking.plusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(0));
    }

    @Test
    void findBookingByBooker_IdAndEndIsBefore() {
        setData();
        List<Booking> bookingList = bookingRepository.findBookingByBooker_IdAndEndIsBefore(user1.getId(),
                endBooking.plusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(1));
        assertThat(bookingList.get(0).getBooker().getId(), is(user1.getId()));

        bookingList = bookingRepository.findBookingByBooker_IdAndEndIsBefore(user1.getId(),
                endBooking.minusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(0));
    }

    @Test
    void findBookingByBooker_IdAndStartIsBeforeAndEndIsAfter() {
        setData();
        List<Booking> bookingList = bookingRepository.
                findBookingByBooker_IdAndStartIsBeforeAndEndIsAfter(user1.getId(),
                startBooking.plusDays(1),startBooking.plusDays(1), Pageable.unpaged());
        assertThat(bookingList, hasSize(1));
        assertThat(bookingList.get(0).getBooker().getId(), is(user1.getId()));

        bookingList = bookingRepository.findBookingByBooker_IdAndStartIsBeforeAndEndIsAfter(user1.getId(),
                startBooking.minusDays(1),startBooking.minusDays(1), Pageable.unpaged());

        assertThat(bookingList, hasSize(0));
    }

    @Test
    void findFirst1BookingByItem_IdAndStartIsBefore() {
        setData();
        Booking bookingFirst = bookingRepository.
                findFirst1BookingByItem_IdAndStartIsBefore(item1.getId(),
                        startBooking.plusDays(1), Sort.by("id").descending());
        assertThat(bookingFirst.getItem().getId(), is(item1.getId()));

        bookingFirst = bookingRepository.
                findFirst1BookingByItem_IdAndStartIsBefore(item1.getId(),
                        startBooking.minusDays(1), Sort.by("id").descending());
        assertThat(bookingFirst, nullValue());
    }

    @Test
    void findFirst1BookingByItem_IdAndStartIsAfter() {
        setData();
        Booking bookingFirst = bookingRepository.
                findFirst1BookingByItem_IdAndStartIsAfter(item1.getId(),
                        startBooking.minusDays(1), Sort.by("id").descending());
        assertThat(bookingFirst.getItem().getId(), is(item1.getId()));

        bookingFirst = bookingRepository.
                findFirst1BookingByItem_IdAndStartIsAfter(item1.getId(),
                        startBooking.plusDays(1), Sort.by("id").descending());
        assertThat(bookingFirst, nullValue());
    }

    @Test
    void findBookingsByBooker_IdAndItem_IdAndStatusAndEndBefore() {
        setData();
        List<Booking> bookingList  = bookingRepository.
                findBookingsByBooker_IdAndItem_IdAndStatusAndEndBefore(user1.getId(), item2.getId(),
                        BookingStatus.WAITING, endBooking.plusDays(1));
        assertThat(bookingList, hasSize(1));
        assertThat(bookingList.get(0).getBooker().getId(), is(user1.getId()));
        assertThat(bookingList.get(0).getItem().getId(), is(item2.getId()));

        bookingList  = bookingRepository.
                findBookingsByBooker_IdAndItem_IdAndStatusAndEndBefore(user1.getId(), item2.getId(),
                        BookingStatus.WAITING, endBooking.minusDays(1));
        assertThat(bookingList, hasSize(0));
    }
}