package org.acme.agentic.services;

import java.util.List;

import org.acme.agentic.model.Hotel;
import org.acme.agentic.model.HotelRequest;

public interface HotelService {
    List<Hotel> searchHotels(HotelRequest request);
}
