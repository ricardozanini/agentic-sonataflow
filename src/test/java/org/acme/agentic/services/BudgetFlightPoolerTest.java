package org.acme.agentic.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.acme.agentic.DisableOllamaProfile;
import org.acme.agentic.model.BudgetPoolRequest;
import org.acme.agentic.model.Flight;
import org.acme.agentic.model.FlightRequest;
import org.acme.agentic.workflows.TravelPlannerFlow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.event.DataEvent;
import org.kie.kogito.serverless.workflow.executor.events.CloudEventReceiver;
import org.kie.kogito.serverless.workflow.executor.events.InMemoryEventShared;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestProfile(DisableOllamaProfile.class)
public class BudgetFlightPoolerTest {

    @Inject
    BudgetFlightPooler pooler;

    @InjectMock
    FlightService flightService;

    @Inject
    TestFlightListener listener;

    @Inject
    ObjectMapper objectMapper;

    @BeforeEach
    void before() {
        listener.reset();
    }

    /**
     * Uses the real ManagedExecutor from SmallRye (injected automatically)
     * and the TestFlightListener above to observe the event.
     */
    @Test
    public void testEarlyExitWhenUnderBudget() throws InterruptedException, ExecutionException, TimeoutException {
        final CloudEventReceiver receiver = new CloudEventReceiver();
        InMemoryEventShared.INSTANCE.receivers().put(TravelPlannerFlow.Events.FLIGHT_POOLER_RESULT, receiver);

        // Arrange
        FlightRequest req = new FlightRequest();
        req.setDestination("Berlin");
        req.setDate(LocalDate.parse("2025-07-10"));
        BudgetPoolRequest poolReq = new BudgetPoolRequest();
        poolReq.setAttempts(3);
        poolReq.setIntervalMs(100L);
        poolReq.setBudget(150.0);

        Flight cheap = new Flight("Air Canada", 100, LocalDateTime.parse("2025-07-10T13:00"));
        when(flightService.searchFlights(req)).thenReturn(List.of(cheap));

        CompletableFuture<FlightPooledEvent> promise = new CompletableFuture<>();

        receiver.subscribe((DataEvent<FlightPooledEvent> evt) -> {
            final FlightPooledEvent flightPooledEvent = evt.getData();
            promise.complete(flightPooledEvent);
            return CompletableFuture.completedFuture(flightPooledEvent);
        }, FlightPooledEvent.class);

        // Act
        Map<String, Object> jsonReqMap = objectMapper.convertValue(req, new TypeReference<>() {
        });
        Map<String, Object> jsonPoolMap = objectMapper.convertValue(poolReq, new TypeReference<>() {
        });
        pooler.findCheaperFlightAsync(jsonReqMap, jsonPoolMap, UUID.randomUUID().toString());

        FlightPooledEvent result = promise.get(1, TimeUnit.SECONDS);
        assertThat(result).isNotNull();
        assertThat(result.getBestFlight().isPresent()).isTrue();
        assertThat(result.getRequest()).isEqualTo(req);
        assertThat(result.getBudget()).isEqualTo(poolReq.getBudget());
        assertThat(result.getBestFlight()).isPresent().get().isEqualTo(cheap);
    }
}
