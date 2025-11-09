package com.aggregator.client;

import com.aggregator.model.FlightDTO;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Functional interface representing a flight provider.
 * Each provider implementation handles its own service call and data transformation.
 */
@FunctionalInterface
public interface FlightProvider {
    /**
     * Searches for flights from this provider.
     * 
     * @param origin Origin airport code
     * @param destination Destination airport code
     * @param departureDate Departure date and time
     * @return List of FlightDTO objects, or empty list if error occurs
     */
    List<FlightDTO> searchFlights(String origin, String destination, LocalDateTime departureDate);
}

