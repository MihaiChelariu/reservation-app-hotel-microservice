package org.chelariulicenta.hotel.services;

import com.github.dozermapper.core.Mapper;
import org.chelariulicenta.hotel.model.Photo;
import org.chelariulicenta.hotel.repositories.PhotoRepository;
import org.chelariulicenta.hotel.view.VPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    Mapper mapper;

    public List<VPhoto> getPhotosByHotelId(Integer id) {
        List<Photo> allByIdHotel = photoRepository.findAllByIdHotel(id);
        return allByIdHotel.stream().map(photo -> mapper.map(photo, VPhoto.class)).collect(Collectors.toList());
    }

    public VPhoto getFirstByHotelId(Integer id){
        List<Photo> allByIdHotel = photoRepository.findAllByIdHotel(id);
        return mapper.map(allByIdHotel.get(0), VPhoto.class);
    }
}
