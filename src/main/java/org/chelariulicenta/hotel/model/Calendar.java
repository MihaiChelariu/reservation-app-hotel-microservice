package org.chelariulicenta.hotel.model;

import jakarta.persistence.*;
import jdk.jfr.Label;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="room_calendar")
@Data
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idroom_calendar")
    private int idCalendar;
    private int idHotel;
    private LocalDate calendarDate;
    private int availableSingleRooms;
    private int availableDoubleRooms;
    private int availablePremiumRooms;
}
