package com.flightprovidera.service;

import jakarta.jws.WebService;

@WebService(endpointInterface = "com.flightprovidera.service.FlightSearchSOAP")
public class FlightSearchSOAPImpl implements FlightSearchSOAP {

    private final SearchService searchService = new SearchService();

    @Override
    public SearchResult availabilitySearch(SearchRequest request) {
        return searchService.availabilitySearch(request);
    }
}
