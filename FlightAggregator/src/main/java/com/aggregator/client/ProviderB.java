package com.aggregator.client;

import com.aggregator.model.FlightDTO;
import com.aggregator.util.LocalDateTimeAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.xml.namespace.QName;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class ProviderB implements FlightProvider {
    private static final Logger logger = Logger.getLogger(ProviderB.class.getName());
    private static final QName QNAME = new QName("http://service.flightproviderb.com/", "FlightSearchSOAPImplService");

    @Value("${flight.provider.b.wsdl.url}")
    private String wsdlUrl;

    // Service instance'ı bir kere oluşturulup tekrar kullanılır
    private volatile com.flightproviderb.service.FlightSearchSOAP service;

    private com.flightproviderb.service.FlightSearchSOAP getService() {
        if (service == null) {
            synchronized (this) {
                if (service == null) {
                    try {
                        URL url = new URL(wsdlUrl);
                        service = jakarta.xml.ws.Service.create(url, QNAME)
                                .getPort(com.flightproviderb.service.FlightSearchSOAP.class);
                    } catch (Exception e) {
                        logger.severe("Failed to create Provider B service: " + e.getMessage());
                        throw new RuntimeException("Provider B service initialization failed", e);
                    }
                }
            }
        }
        return service;
    }

    @Override
    public List<FlightDTO> searchFlights(String origin, String destination, LocalDateTime departureDate) {
        try {
            com.flightproviderb.service.FlightSearchSOAP service = getService();

            com.flightproviderb.service.SearchRequest request = new com.flightproviderb.service.SearchRequest();
            request.setDeparture(origin);
            request.setArrival(destination);
            request.setDepartureDate(LocalDateTimeAdapter.marshal(departureDate));

            com.flightproviderb.service.SearchResult result = service.availabilitySearch(request);
            if (result.isHasError()) {
                logger.warning("Provider B reported an error: " + result.getErrorMessage());
                return List.of();
            }
            return result.getFlightOptions()
                    .stream()
                    .map(FlightDTO::fromProviderB)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.warning("Provider B call failed: " + e.getMessage());
            return List.of();
        }
    }
}
