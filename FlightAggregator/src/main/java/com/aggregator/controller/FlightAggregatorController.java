package com.aggregator.controller;

import com.aggregator.client.FlightAggregatorClient;
import com.aggregator.model.FlightDTO;
import com.aggregator.util.FlightGrouper;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightAggregatorController {

    private final FlightAggregatorClient client = new FlightAggregatorClient();

    @GetMapping
    public List<FlightDTO> search(@RequestParam String origin, @RequestParam String destination, @RequestParam String departureDate) throws Exception {
        LocalDateTime dt = LocalDateTime.parse(departureDate);
        return client.getAllFlights(origin, destination, dt);
    }
    
    @GetMapping("/cheapest")
    public List<FlightDTO> searchCheapest(@RequestParam String origin, @RequestParam String destination, @RequestParam String departureDate) throws Exception {
        LocalDateTime dt = LocalDateTime.parse(departureDate);
        List<FlightDTO> allFlights = client.getAllFlights(origin, destination, dt);
        return FlightGrouper.getCheapestPerGroup(allFlights);
    }

}
