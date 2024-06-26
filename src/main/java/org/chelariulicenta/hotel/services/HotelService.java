package org.chelariulicenta.hotel.services;

import com.github.dozermapper.core.Mapper;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.chelariulicenta.hotel.model.Hotel;
import org.chelariulicenta.hotel.model.Calendar;
import org.chelariulicenta.hotel.repositories.HotelRepository;
import org.chelariulicenta.hotel.repositories.CalendarRepository;
import org.chelariulicenta.hotel.view.VCalendar;
import org.chelariulicenta.hotel.view.VHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.IntStream;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private CalendarRepository calendarRepository;

    @Transactional(readOnly = true)
    public VHotel getHotelsById(Integer id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Id" + id + "not found!"));
        return mapper.map(hotel, VHotel.class);
    }

    @Transactional(readOnly = true)
    public List<VHotel> getAllHotels() {
        List<Hotel> hotelList = hotelRepository.findAll();
        List<VHotel> vHotelList = hotelList.stream().map(hotel -> mapper.map(hotel, VHotel.class)).toList();
        return vHotelList;
    }

    @Transactional(readOnly = true)
    public List<VHotel> getHotelsByLocation(String location, LocalDate checkin, LocalDate checkout, Integer people) {
        List<Hotel> hotelList = hotelRepository.findByHotelLocation(location);
        return hotelList.stream()
                .filter(hotel -> isRoomAvailable(hotel, checkin, checkout, people))
                .map(hotel -> mapper.map(hotel, VHotel.class))
                .toList();
    }

    private boolean isRoomAvailable(Hotel hotel, LocalDate checkin, LocalDate checkout, Integer people) {
        List<Calendar> calendars = calendarRepository.findByIdHotelAndCalendarDateBetween(hotel.getIdHotel(), checkin, checkout);

        if (calendars.isEmpty())
            return false;

        int singleRoomsNeeded = people;
        int doubleRoomsNeeded = (int) Math.ceil(people / 2.0);
        int premiumRoomsNeeded = (int) Math.ceil(people / 3.0);

        for (Calendar calendar : calendars) {
            if (singleRoomsNeeded > calendar.getAvailableSingleRooms() &&
                    doubleRoomsNeeded > calendar.getAvailableDoubleRooms() &&
                    premiumRoomsNeeded > calendar.getAvailablePremiumRooms()) {
                return false;
            }
        }
        return true;
    }

    public ResponseEntity updateCalendarAfterSave(int hotelId, LocalDate checkin, LocalDate checkout, int singleRooms, int doubleRooms, int premiumRooms) {
        List<Calendar> calendars = calendarRepository.findByIdHotelAndCalendarDateBetween(hotelId, checkin, checkout);
        StringBuilder builder = new StringBuilder();
        boolean isAvailable = true;

        for (Calendar calendar : calendars) {

            int availableSingleRooms = calendar.getAvailableSingleRooms();
            int availableDoubleRooms = calendar.getAvailableDoubleRooms();
            int availablePremiumRooms = calendar.getAvailablePremiumRooms();
            if (calendar.getAvailableSingleRooms() < singleRooms ||
                    calendar.getAvailableDoubleRooms() < doubleRooms ||
                    calendar.getAvailablePremiumRooms() < premiumRooms) {
                isAvailable = false;
                break;
            }


        if (!isAvailable) {
            return ResponseEntity.status(400).body("Insufficient rooms available for the requested period.");
        }
            if (availableSingleRooms > 0 && availableSingleRooms >= singleRooms && singleRooms != 0) {
                calendar.setAvailableSingleRooms(availableSingleRooms - singleRooms);
                builder.append("single ");
            }
            if(availableDoubleRooms > 0 && availableDoubleRooms >= doubleRooms &&doubleRooms != 0){
                calendar.setAvailableDoubleRooms(availableDoubleRooms - doubleRooms);
                builder.append("double ");
            }
            if(availablePremiumRooms > 0 && availablePremiumRooms >= premiumRooms && premiumRooms != 0){
                calendar.setAvailablePremiumRooms(availablePremiumRooms - premiumRooms);
                builder.append("premium");
            }
        }
        calendarRepository.flush();
        return ResponseEntity.status(200).body(builder.toString());
    }

    public ResponseEntity updateCalendarAfterCancel(int hotelId, LocalDate checkin, LocalDate checkout, int singleRooms, int doubleRooms, int premiumRooms) {
        List<Calendar> calendars = calendarRepository.findByIdHotelAndCalendarDateBetween(hotelId, checkin, checkout);
        StringBuilder builder = new StringBuilder();

        for (Calendar calendar : calendars) {
            int availableSingleRooms = calendar.getAvailableSingleRooms();
            int availableDoubleRooms = calendar.getAvailableDoubleRooms();
            int availablePremiumRooms = calendar.getAvailablePremiumRooms();
            if (singleRooms != 0) {
                calendar.setAvailableSingleRooms(availableSingleRooms + singleRooms);
                builder.append("single ");
            }
            if(doubleRooms != 0){
                calendar.setAvailableDoubleRooms(availableDoubleRooms + doubleRooms);
                builder.append("double ");
            }
            if(premiumRooms != 0){
                calendar.setAvailablePremiumRooms(availablePremiumRooms + premiumRooms);
                builder.append("premium");
            }
        }
        calendarRepository.flush();
        return ResponseEntity.status(200).body(builder.toString());
    }

    public Hotel reserveCameras(Integer id, Integer singleCameras, Integer doubleCameras, Integer premiumCameras) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Id" + id + "not found!"));

        if (hotel.getSingleCameras() - singleCameras >= 0) {
            hotel.setSingleCameras(hotel.getSingleCameras() - singleCameras);
        } else {
            throw new RuntimeException("No more single rooms available!");
        }

        if (hotel.getDoubleCameras() - doubleCameras >= 0) {
            hotel.setDoubleCameras(hotel.getDoubleCameras() - doubleCameras);
        } else {
            throw new RuntimeException("No more double rooms available!");
        }

        if (hotel.getPremiumCameras() - premiumCameras >= 0) {
            hotel.setPremiumCameras(hotel.getPremiumCameras() - premiumCameras);
        } else {
            throw new RuntimeException("No more premium rooms available!");
        }

        return hotelRepository.saveAndFlush(hotel);
    }

    public VHotel saveHotel(VHotel vHotel) {
        Hotel hotel = mapper.map(vHotel, Hotel.class);
        hotelRepository.saveAndFlush(hotel);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

        List<Calendar> calendarEntries = new ArrayList<>();

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            Calendar calendar = new Calendar();
            calendar.setIdHotel(hotel.getIdHotel());
            calendar.setCalendarDate(date);
            calendar.setAvailableSingleRooms(hotel.getSingleCameras());
            calendar.setAvailableDoubleRooms(hotel.getDoubleCameras());
            calendar.setAvailablePremiumRooms(hotel.getPremiumCameras());
            calendarEntries.add(calendar);
        }

        calendarRepository.saveAllAndFlush(calendarEntries);

        return vHotel;
    }

    public void populateDatabase(List<VHotel> vHotels) {
        for (int idx = 1; idx <= 50; idx++) {
            for (VHotel vHotel : vHotels) {
                VHotel newHotel = new VHotel();
                newHotel.setHotelName(vHotel.getHotelName()  + Integer.toString(idx));
                newHotel.setHotelLocation(vHotel.getHotelLocation());
                newHotel.setSingleCameras(vHotel.getSingleCameras());
                newHotel.setDoubleCameras(vHotel.getDoubleCameras());
                newHotel.setPremiumCameras(vHotel.getPremiumCameras());
                newHotel.setHotelDescription(vHotel.getHotelDescription());
                newHotel.setHotelRating(vHotel.getHotelRating());
                newHotel.setSingleCameraPrice(vHotel.getSingleCameraPrice());
                newHotel.setDoubleCameraPrice(vHotel.getDoubleCameraPrice());
                newHotel.setPremiumCameraPrice(vHotel.getPremiumCameraPrice());
                saveHotel(newHotel);
            }
        }
    }

    public List<VCalendar> roomsAvailable(int id, LocalDate checkin, LocalDate checkout) {
        List<Calendar> calendars = calendarRepository.findByIdHotelAndCalendarDateBetween(id, checkin, checkout);
        return calendars.stream().map(calendar -> mapper.map(calendar, VCalendar.class)).toList();
    }

    @Transactional(readOnly = true)
    public VHotel getHotelsByName(String name){
        Hotel hotel = hotelRepository.findByHotelName(name);
        return mapper.map(hotel, VHotel.class);
    }

    public void deleteHotelById (int id) {
        hotelRepository.deleteByIdHotel(id);
    }
}
