package com.aggregator.util;

import com.aggregator.model.FlightDTO;
import java.util.*;

public class FlightGrouper {

    public static List<FlightDTO> getCheapestPerGroup(List<FlightDTO> flights) {

        // Grup anahtarı: uçuş numarası + kalkış yeri + varış yeri + kalkış saati + varış saati
        Map<String, FlightDTO> groupedMap = new HashMap<>();

        for (FlightDTO flight : flights) {
            String key = flight.getFlightNumber() + "|" +
                         flight.getDeparture() + "|" +
                         flight.getArrival() + "|" +
                         flight.getDepartureDateTime() + "|" +
                         flight.getArrivalDateTime();

            if (!groupedMap.containsKey(key)) {
                groupedMap.put(key, flight);
            } else {
                FlightDTO existing = groupedMap.get(key);
                if (flight.getPrice() < existing.getPrice()) {
                    groupedMap.put(key, flight);
                }
            }
        }

        return new ArrayList<>(groupedMap.values());
    }
}
