package org.acme.agentic.services;

import java.util.List;

import org.acme.agentic.model.Hotel;
import org.acme.agentic.model.HotelRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StubHotelService implements HotelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StubHotelService.class);

    @Inject
    StubUtils utils;

    @Tool("search for hotels based on location, max price per night, and number of nights")
    @Override
    public List<Hotel> searchHotels(HotelRequest request) {
        LOGGER.info("Searching for hotels based on location, max price, and number of nights {}", request);
        return utils.loadList("hotels.json", new TypeReference<>() {
        });
    }
}
