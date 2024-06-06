package org.chelariulicenta.hotel.repositories;

import org.chelariulicenta.hotel.model.Photo;
import org.chelariulicenta.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    List<Photo> findAllByIdHotel(Integer id);
}
