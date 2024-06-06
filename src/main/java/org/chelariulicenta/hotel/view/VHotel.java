package org.chelariulicenta.hotel.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VHotel implements Serializable{

    @JsonProperty
    private int idHotel;
    @JsonProperty
    private String hotelName;
    @JsonProperty
    private String hotelLocation;
    @JsonProperty
    private String hotelDescription;
    @JsonProperty
    private int hotelRating;
    @JsonProperty
    private int singleCameras;
    @JsonProperty
    private int doubleCameras;
    @JsonProperty
    private int premiumCameras;
    @JsonProperty
    private int singleCameraPrice;
    @JsonProperty
    private int doubleCameraPrice;
    @JsonProperty
    private int premiumCameraPrice;
}
