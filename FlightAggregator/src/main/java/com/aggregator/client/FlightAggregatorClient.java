package com.aggregator.client;

import com.aggregator.model.FlightDTO;
import com.aggregator.util.LocalDateTimeAdapter;
import jakarta.xml.ws.Service;
import javax.xml.namespace.QName;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public class FlightAggregatorClient {
	private static final Logger logger = Logger.getLogger(FlightAggregatorClient.class.getName());

    public List<FlightDTO> getAllFlights(String origin, String destination, LocalDateTime departureDate) throws Exception {
        List<FlightDTO> allFlights = new ArrayList<>();
        
        // Provider A
        URL urlA = new URL("http://localhost:8080/FlightSearchSOAP?wsdl");
        QName qnameA = new QName("http://service.flightprovidera.com/", "FlightSearchSOAPImplService");
        com.flightprovidera.service.FlightSearchSOAP serviceA = Service.create(urlA, qnameA).getPort(com.flightprovidera.service.FlightSearchSOAP.class);

        com.flightprovidera.service.SearchRequest reqA = new com.flightprovidera.service.SearchRequest();
        reqA.setDestination(destination);
        reqA.setOrigin(origin);
        reqA.setDepartureDate(LocalDateTimeAdapter.marshal(departureDate));

        com.flightprovidera.service.SearchResult resA = serviceA.availabilitySearch(reqA);
        
        if (resA.isHasError())
        	logger.warning("Provider A reported an error: " + resA.getErrorMessage());
        else        	
	        allFlights.addAll(resA.getFlightOptions()
	                        .stream()
	                        .map(FlightDTO::fromProviderA)
	                        .collect(Collectors.toList())
	        );

        // Provider B
        URL urlB = new URL("http://localhost:8081/FlightSearchSOAP?wsdl");
        QName qnameB = new QName("http://service.flightproviderb.com/", "FlightSearchSOAPImplService");
        com.flightproviderb.service.FlightSearchSOAP serviceB = Service.create(urlB, qnameB).getPort(com.flightproviderb.service.FlightSearchSOAP.class);

        com.flightproviderb.service.SearchRequest reqB = new com.flightproviderb.service.SearchRequest();
        reqB.setDeparture(origin);
        reqB.setArrival(destination);
        reqB.setDepartureDate(LocalDateTimeAdapter.marshal(departureDate));
        
        com.flightproviderb.service.SearchResult resB = serviceB.availabilitySearch(reqB);

        if (resB.isHasError())
        	logger.warning("Provider B reported an error: " + resB.getErrorMessage());
        else        	
	        allFlights.addAll(resB.getFlightOptions()
	                        .stream()
	                        .map(FlightDTO::fromProviderB)
	                        .collect(Collectors.toList())
	        );
        

        return allFlights;
    }    
    
}
