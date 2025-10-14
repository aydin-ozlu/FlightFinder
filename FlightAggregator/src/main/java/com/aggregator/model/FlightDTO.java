package com.aggregator.model;

import java.time.LocalDateTime;
import com.aggregator.util.LocalDateTimeAdapter;

public class FlightDTO {
    private String flightNumber;
    private String departure;
    private String arrival;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private double price;

    public static FlightDTO fromProviderA(com.flightprovidera.service.Flight f) {
        if (f == null) return null;

        FlightDTO dto = new FlightDTO();
        dto.setFlightNumber(f.getFlightNo());
        dto.setDeparture(f.getOrigin());
        dto.setArrival(f.getDestination());
        dto.setDepartureDateTime(LocalDateTimeAdapter.unmarshal(f.getDeparturedatetime()));
        dto.setArrivalDateTime(LocalDateTimeAdapter.unmarshal(f.getArrivaldatetime()));
        dto.setPrice(f.getPrice() != null ? f.getPrice().doubleValue() : 0.0);
        return dto;
    }
    
    public static FlightDTO fromProviderB(com.flightproviderb.service.Flight f) {
        if (f == null) return null;

        FlightDTO dto = new FlightDTO();
        dto.setFlightNumber(f.getFlightNumber());
        dto.setDeparture(f.getDeparture());
        dto.setArrival(f.getArrival());
        dto.setDepartureDateTime(LocalDateTimeAdapter.unmarshal(f.getDeparturedatetime()));
        dto.setArrivalDateTime(LocalDateTimeAdapter.unmarshal(f.getArrivaldatetime()));
        dto.setPrice(f.getPrice() != null ? f.getPrice().doubleValue() : 0.0);
        return dto;
    }
    
    
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }

    public String getArrival() { return arrival; }
    public void setArrival(String arrival) { this.arrival = arrival; }

    public LocalDateTime getDepartureDateTime() { return departureDateTime; }
    public void setDepartureDateTime(LocalDateTime departureDateTime) { this.departureDateTime = departureDateTime; }

    public LocalDateTime getArrivalDateTime() { return arrivalDateTime; }
    public void setArrivalDateTime(LocalDateTime arrivalDateTime) { this.arrivalDateTime = arrivalDateTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
