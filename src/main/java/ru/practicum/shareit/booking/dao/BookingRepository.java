package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;

import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBooker_IdOrderByStartDateDesc(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.startDate < CURRENT_TIMESTAMP AND b.endDate > CURRENT_TIMESTAMP ORDER BY b.startDate DESC")
    Collection<Booking> findCurrentByBooker_Id(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.endDate < CURRENT_TIMESTAMP ORDER BY b.startDate DESC")
    Collection<Booking> findPastByBooker_Id(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.startDate > CURRENT_TIMESTAMP ORDER BY b.startDate DESC")
    Collection<Booking> findFutureByBooker_Id(Long bookerId);

    Collection<Booking> findAllByBooker_IdAndStatusOrderByStartDateDesc(Long bookerId, StatusBooking status);


    Collection<Booking> findAllByItem_Owner_IdOrderByStartDateDesc(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.startDate < CURRENT_TIMESTAMP AND b.endDate > CURRENT_TIMESTAMP ORDER BY b.startDate DESC")
    Collection<Booking> findCurrentByOwner_Id(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.endDate < CURRENT_TIMESTAMP ORDER BY b.startDate DESC")
    Collection<Booking> findPastByOwner_Id(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.startDate > CURRENT_TIMESTAMP ORDER BY b.startDate DESC")
    Collection<Booking> findFutureByOwner_Id(Long ownerId);

    Collection<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDateDesc(Long ownerId, StatusBooking status);


    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.item.id = :itemId AND b.endDate < CURRENT_TIMESTAMP ORDER BY b.startDate DESC")
    Optional<Booking> findPastByItem_IdAndBooker_Id(Long itemId, Long bookerId);
}