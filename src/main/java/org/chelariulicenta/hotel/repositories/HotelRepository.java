package org.chelariulicenta.hotel.repositories;

import org.chelariulicenta.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findByHotelLocation(String location);
    Hotel findByHotelName(String name);
    void deleteByIdHotel(int id);
}