package com.flightproviderb.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface FlightSearchSOAP {
    @WebMethod
    SearchResult availabilitySearch(SearchRequest request);
}
