package org.chelariulicenta.hotel.model;

import jakarta.persistence.*;
import jdk.jfr.Label;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="photos")
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPhoto;
    private int idHotel;
    private String urlPhoto;
    private String photoType;
}
