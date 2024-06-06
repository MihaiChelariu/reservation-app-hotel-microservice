package org.chelariulicenta.hotel.repositories;

import org.chelariulicenta.hotel.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    List<Calendar> findByIdHotelAndCalendarDateBetween(Integer hotelId, LocalDate startDate, LocalDate endDate);
}