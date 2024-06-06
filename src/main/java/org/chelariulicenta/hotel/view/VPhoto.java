package org.chelariulicenta.hotel.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VPhoto {

    @JsonProperty
    private int idHotel;
    @JsonProperty
    private String urlPhoto;
}
