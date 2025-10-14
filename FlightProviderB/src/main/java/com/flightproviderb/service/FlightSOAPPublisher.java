package com.flightproviderb.service;

import jakarta.xml.ws.Endpoint;

public class FlightSOAPPublisher {
    public static void main(String[] args) {
        String url = "http://localhost:8081/FlightSearchSOAP";
        Endpoint.publish(url, new FlightSearchSOAPImpl());
        System.out.println("FlightProviderB SOAP Service running at: " + url + "?wsdl");
    }
}
