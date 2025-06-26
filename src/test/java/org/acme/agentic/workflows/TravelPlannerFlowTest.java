package org.acme.agentic.workflows;

import org.acme.agentic.DisableOllamaProfile;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestProfile(DisableOllamaProfile.class)
class TravelPlannerFlowTest {

    @Inject
    TravelPlannerFlow travelPlannerFlow;

    // Simple test to validate workflow build. Necessary to avoid loading llama all the time for simple checks.
    @Test
    void flightPriceWatcherFlow() {
        assertNotNull(travelPlannerFlow.flightPriceWatcherFlow());
    }
}