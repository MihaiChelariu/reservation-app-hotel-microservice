package org.chelariulicenta.hotel.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VCalendar {

    @JsonProperty
    private int idHotel;
    @JsonProperty
    private LocalDate calendarDate;
    @JsonProperty
    private int availableSingleRooms;
    @JsonProperty
    private int availableDoubleRooms;
    @JsonProperty
    private int availablePremiumRooms;
}
