package org.acme.agentic.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StubUtils {
    @Inject
    ObjectMapper mapper;

    public <T> List<T> loadList(String resourcePath, TypeReference<List<T>> type) {
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("mock-data/" + resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("Stub data not found: mock-data/" + resourcePath);
        }
        try {
            return mapper.readValue(is, type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load stub data: " + resourcePath, e);
        }
    }
}
