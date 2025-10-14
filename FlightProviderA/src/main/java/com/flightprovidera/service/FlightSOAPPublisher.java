package com.flightprovidera.service;

import jakarta.xml.ws.Endpoint;

public class FlightSOAPPublisher {
    public static void main(String[] args) {
        String url = "http://localhost:8080/FlightSearchSOAP";
        Endpoint.publish(url, new FlightSearchSOAPImpl());
        System.out.println("FlightProviderA SOAP Service running at: " + url + "?wsdl");
    }
}
