package com.aggregator.controller;

import com.aggregator.model.FlightDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FlightAggregatorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private com.aggregator.client.FlightClient mockClient;

    @InjectMocks
    private FlightAggregatorController controller;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void search_shouldReturnFlights_whenClientReturnsData() throws Exception {
        LocalDateTime dt = LocalDateTime.parse("2025-11-05T10:00:00");

        FlightDTO f1 = new FlightDTO();
        f1.setFlightNumber("FN1");
        f1.setDeparture("AAA");
        f1.setArrival("BBB");
        f1.setDepartureDateTime(dt);
        f1.setArrivalDateTime(dt.plusHours(2));
        f1.setPrice(100.0);

        FlightDTO f2 = new FlightDTO();
        f2.setFlightNumber("FN2");
        f2.setDeparture("AAA");
        f2.setArrival("BBB");
        f2.setDepartureDateTime(dt);
        f2.setArrivalDateTime(dt.plusHours(3));
        f2.setPrice(150.0);

        when(mockClient.getAllFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(f1, f2));

        mockMvc.perform(get("/flights")
                .param("origin", "AAA")
                .param("destination", "BBB")
                .param("departureDate", "2025-11-05T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].flightNumber").value(is("FN1")));
    }

    @Test
    public void searchCheapest_shouldReturnCheapestPerGroup_whenClientReturnsDuplicates() throws Exception {
        LocalDateTime dt = LocalDateTime.parse("2025-11-05T10:00:00");

        // two entries that are same group key but different prices
        FlightDTO cheaper = new FlightDTO();
        cheaper.setFlightNumber("G1");
        cheaper.setDeparture("AAA");
        cheaper.setArrival("BBB");
        cheaper.setDepartureDateTime(dt);
        cheaper.setArrivalDateTime(dt.plusHours(2));
        cheaper.setPrice(80.0);

        FlightDTO expensive = new FlightDTO();
        expensive.setFlightNumber("G1");
        expensive.setDeparture("AAA");
        expensive.setArrival("BBB");
        expensive.setDepartureDateTime(dt);
        expensive.setArrivalDateTime(dt.plusHours(2));
        expensive.setPrice(120.0);

        // different flight
        FlightDTO other = new FlightDTO();
        other.setFlightNumber("G2");
        other.setDeparture("CCC");
        other.setArrival("DDD");
        other.setDepartureDateTime(dt.plusDays(1));
        other.setArrivalDateTime(dt.plusDays(1).plusHours(2));
        other.setPrice(200.0);

        List<FlightDTO> returned = Arrays.asList(expensive, cheaper, other);
        when(mockClient.getAllFlights(anyString(), anyString(), any(LocalDateTime.class))).thenReturn(returned);

        mockMvc.perform(get("/flights/cheapest")
                .param("origin", "AAA")
                .param("destination", "BBB")
                .param("departureDate", "2025-11-05T10:00:00"))
                .andExpect(status().isOk())
                // we expect 2 groups after collapsing (G1->cheaper, G2->other)
                .andExpect(jsonPath("$", hasSize(2)))
                // ensure the cheaper price is present in returned prices
                .andExpect(jsonPath("$[*].price", hasItem(80.0)));
    }

    @Test
    public void search_shouldReturnServerError_whenClientThrows() throws Exception {
        when(mockClient.getAllFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenThrow(new RuntimeException("provider failure"));

        mockMvc.perform(get("/flights")
                .param("origin", "AAA")
                .param("destination", "BBB")
                .param("departureDate", "2025-11-05T10:00:00"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void searchCheapest_shouldReturnServerError_whenClientThrows() throws Exception {
        when(mockClient.getAllFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenThrow(new RuntimeException("provider failure"));

        mockMvc.perform(get("/flights/cheapest")
                .param("origin", "AAA")
                .param("destination", "BBB")
                .param("departureDate", "2025-11-05T10:00:00"))
                .andExpect(status().is5xxServerError());
    }
}
