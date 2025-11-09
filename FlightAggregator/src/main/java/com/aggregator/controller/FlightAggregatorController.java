package com.aggregator.controller;

import com.aggregator.model.FlightDTO;
import com.aggregator.util.FlightGrouper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightAggregatorController {
    private final com.aggregator.client.FlightClient client;

    public FlightAggregatorController(com.aggregator.client.FlightClient client) {
        this.client = client;
    }

    @GetMapping
    public List<FlightDTO> search(@RequestParam("origin") String origin,
            @RequestParam("destination") String destination,
            @RequestParam("departureDate") String departureDate) throws Exception {
        LocalDateTime dt = LocalDateTime.parse(departureDate);
        try {
            return client.getAllFlights(origin, destination, dt);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping("/cheapest")
    public List<FlightDTO> searchCheapest(@RequestParam("origin") String origin,
            @RequestParam("destination") String destination,
            @RequestParam("departureDate") String departureDate) throws Exception {
        LocalDateTime dt = LocalDateTime.parse(departureDate);
        try {
            List<FlightDTO> allFlights = client.getAllFlights(origin, destination, dt);
            return FlightGrouper.getCheapestPerGroup(allFlights);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}
