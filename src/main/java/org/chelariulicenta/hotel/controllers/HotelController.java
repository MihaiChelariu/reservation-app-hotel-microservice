package org.chelariulicenta.hotel.controllers;

import org.chelariulicenta.hotel.model.Calendar;
import org.chelariulicenta.hotel.model.Hotel;
import org.chelariulicenta.hotel.services.HotelService;
import org.chelariulicenta.hotel.view.VCalendar;
import org.chelariulicenta.hotel.view.VHotel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@CrossOrigin
public class HotelController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HotelController.class);
    @Autowired
    private HotelService hotelService;

    @GetMapping("/getAllHotels")
    private List<VHotel> getHotel() {
        List<VHotel> allHotels = hotelService.getAllHotels();
        LOGGER.info("GET all hotels");
        return allHotels;
    }

    @GetMapping("/getHotel/{id}")
    private VHotel getHotel(@PathVariable("id") Integer id) {
        VHotel vHotel = hotelService.getHotelsById(id);
        LOGGER.info("GET " + id);
        return vHotel;
    }

    @GetMapping("/getHotelsByLocation/{location}")
    private List<VHotel> getHotelByLocation(@PathVariable("location") String location,
                                            @RequestParam("checkin") LocalDate checkin,
                                            @RequestParam("checkout") LocalDate checkout,
                                            @RequestParam("people") Integer people) {
        List<VHotel> hotels = hotelService.getHotelsByLocation(location, checkin, checkout, people);
        LOGGER.info("GET " + location);
        return hotels;
    }

    @PutMapping("/updateCameras/{hotelId}")
    private ResponseEntity<String> reserveCameras(
            @PathVariable(name = "hotelId") Integer id,
            @RequestParam("checkin") String checkin,
            @RequestParam("checkout") String checkout,
            @RequestParam("singleCameras") Integer singleCameras,
            @RequestParam("doubleCameras") Integer doubleCameras,
            @RequestParam("premiumCameras") Integer premiumCameras) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parse = LocalDate.parse(checkout, formatter);

            ResponseEntity responseEntity = hotelService.updateCalendarAfterSave(id, LocalDate.parse(checkin, formatter), LocalDate.parse(checkout, formatter), singleCameras, doubleCameras, premiumCameras);
            return responseEntity;
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/saveHotel")
    private VHotel saveHotel(@RequestBody VHotel vHotel) {
        VHotel saveHotel = hotelService.saveHotel(vHotel);
        return saveHotel;
    }

    @GetMapping("/getRoomsAvailable/{hotelId}")
    private List<VCalendar> roomsAvailable(@PathVariable(name = "hotelId") Integer id,
                                           @RequestParam("checkin") String checkin,
                                           @RequestParam("checkout") String checkout) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<VCalendar> vCalendars = hotelService.roomsAvailable(id, LocalDate.parse(checkin, formatter), LocalDate.parse(checkout, formatter));
        return vCalendars;
    }

    @GetMapping("/getHotelByName/{name}")
    private VHotel getHotelByName (@PathVariable String name){
        return hotelService.getHotelsByName(name);
    }

    @DeleteMapping("/deleteHotelById/{hotelId}")
    private void deleteHotelById(@PathVariable (name = "hotelId") int id){
        hotelService.deleteHotelById(id);
    }
}
