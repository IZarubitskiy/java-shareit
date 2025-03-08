package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBooker_IdOrderByStartDateDesc(Long bookerId);

    Collection<Booking> findByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long bookerId, LocalDateTime now1, LocalDateTime now2);

    Collection<Booking> findByBooker_IdAndEndDateBeforeOrderByStartDateDesc(Long bookerId, LocalDateTime now);

    Collection<Booking> findByBooker_IdAndStartDateAfterOrderByStartDateDesc(Long bookerId, LocalDateTime now);

    Collection<Booking> findAllByBooker_IdAndStatusOrderByStartDateDesc(Long bookerId, StatusBooking status);


    Collection<Booking> findAllByItem_Owner_IdOrderByStartDateDesc(Long ownerId);

    Collection<Booking> findByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long ownerId, LocalDateTime now1, LocalDateTime now2);

    Collection<Booking> findByItem_Owner_IdAndEndDateBeforeOrderByStartDateDesc(Long ownerId, LocalDateTime now);

    Collection<Booking> findByItem_Owner_IdAndStartDateAfterOrderByStartDateDesc(Long ownerId, LocalDateTime now);

    Collection<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDateDesc(Long ownerId, StatusBooking status);


    Collection<Booking> findByItem_IdAndBooker_IdAndEndDateBeforeOrderByStartDateDesc(Long itemId, Long bookerId, LocalDateTime now);
}