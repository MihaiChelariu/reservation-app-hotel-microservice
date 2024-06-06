package org.chelariulicenta.hotel.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hotels")
@Data
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHotel;
    private String hotelName;
    private String hotelLocation;
    private String hotelDescription;
    private int hotelRating;
    @Column(name = "nr_single_cameras")
    private int singleCameras;
    @Column(name = "nr_double_cameras")
    private int doubleCameras;
    @Column(name = "nr_premium_cameras")
    private int premiumCameras;
    @Column(name = "single_camera_price")
    private int singleCameraPrice;
    @Column(name = "double_camera_price")
    private int doubleCameraPrice;
    @Column(name = "premium_camera_price")
    private int premiumCameraPrice;
}
