package org.chelariulicenta.hotel.controllers;

import org.chelariulicenta.hotel.services.PhotoService;
import org.chelariulicenta.hotel.view.VPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin
public class PhotoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);
    @Autowired
    private PhotoService photoService;

    @GetMapping("/getAllByHotelId/{id}")
    public List<VPhoto> getAllByHotelId(@PathVariable Integer id) {
        return photoService.getPhotosByHotelId(id);
    }

    @GetMapping("/getFirstByHotelId/{id}")
    public VPhoto getFirstByHotelId(@PathVariable Integer id){
        return photoService.getFirstByHotelId(id);
    }
}
