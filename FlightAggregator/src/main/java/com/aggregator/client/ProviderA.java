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
public class ProviderA implements FlightProvider {
    private static final Logger logger = Logger.getLogger(ProviderA.class.getName());
    private static final QName QNAME = new QName("http://service.flightprovidera.com/", "FlightSearchSOAPImplService");

    @Value("${flight.provider.a.wsdl.url}")
    private String wsdlUrl;

    // Service instance'ı bir kere oluşturulup tekrar kullanılır
    private volatile com.flightprovidera.service.FlightSearchSOAP service;

    private com.flightprovidera.service.FlightSearchSOAP getService() {
        if (service == null) {
            synchronized (this) {
                if (service == null) {
                    try {
                        URL url = new URL(wsdlUrl);
                        service = jakarta.xml.ws.Service.create(url, QNAME)
                                .getPort(com.flightprovidera.service.FlightSearchSOAP.class);
                    } catch (Exception e) {
                        logger.severe("Failed to create Provider A service: " + e.getMessage());
                        throw new RuntimeException("Provider A service initialization failed", e);
                    }
                }
            }
        }
        return service;
    }

    @Override
    public List<FlightDTO> searchFlights(String origin, String destination, LocalDateTime departureDate) {
        try {
            com.flightprovidera.service.FlightSearchSOAP service = getService();

            com.flightprovidera.service.SearchRequest request = new com.flightprovidera.service.SearchRequest();
            request.setDestination(destination);
            request.setOrigin(origin);
            request.setDepartureDate(LocalDateTimeAdapter.marshal(departureDate));

            com.flightprovidera.service.SearchResult result = service.availabilitySearch(request);
            if (result.isHasError()) {
                logger.warning("Provider A reported an error: " + result.getErrorMessage());
                return List.of();
            }
            return result.getFlightOptions()
                    .stream()
                    .map(FlightDTO::fromProviderA)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.warning("Provider A call failed: " + e.getMessage());
            return List.of();
        }
    }
}
