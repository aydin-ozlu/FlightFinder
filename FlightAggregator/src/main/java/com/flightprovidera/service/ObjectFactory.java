
package com.flightprovidera.service;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.flightprovidera.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AvailabilitySearch_QNAME = new QName("http://service.flightprovidera.com/", "availabilitySearch");
    private final static QName _AvailabilitySearchResponse_QNAME = new QName("http://service.flightprovidera.com/", "availabilitySearchResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.flightprovidera.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AvailabilitySearch }
     * 
     */
    public AvailabilitySearch createAvailabilitySearch() {
        return new AvailabilitySearch();
    }

    /**
     * Create an instance of {@link AvailabilitySearchResponse }
     * 
     */
    public AvailabilitySearchResponse createAvailabilitySearchResponse() {
        return new AvailabilitySearchResponse();
    }

    /**
     * Create an instance of {@link SearchRequest }
     * 
     */
    public SearchRequest createSearchRequest() {
        return new SearchRequest();
    }

    /**
     * Create an instance of {@link SearchResult }
     * 
     */
    public SearchResult createSearchResult() {
        return new SearchResult();
    }

    /**
     * Create an instance of {@link Flight }
     * 
     */
    public Flight createFlight() {
        return new Flight();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AvailabilitySearch }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AvailabilitySearch }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.flightprovidera.com/", name = "availabilitySearch")
    public JAXBElement<AvailabilitySearch> createAvailabilitySearch(AvailabilitySearch value) {
        return new JAXBElement<AvailabilitySearch>(_AvailabilitySearch_QNAME, AvailabilitySearch.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AvailabilitySearchResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AvailabilitySearchResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.flightprovidera.com/", name = "availabilitySearchResponse")
    public JAXBElement<AvailabilitySearchResponse> createAvailabilitySearchResponse(AvailabilitySearchResponse value) {
        return new JAXBElement<AvailabilitySearchResponse>(_AvailabilitySearchResponse_QNAME, AvailabilitySearchResponse.class, null, value);
    }

}
