package com.aggregator.client;

import com.aggregator.model.FlightDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightClient {
    List<FlightDTO> getAllFlights(String origin, String destination, LocalDateTime departureDate) throws Exception;
}
