package org.acme.agentic.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.acme.agentic.model.BudgetPoolRequest;
import org.acme.agentic.model.Flight;
import org.acme.agentic.model.FlightRequest;
import org.acme.agentic.workflows.TravelPlannerFlow;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.kie.kogito.serverless.workflow.executor.events.InMemoryEventShared;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BudgetFlightPooler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BudgetFlightPooler.class);

    @Inject
    FlightService flightService;

    @Inject
    ManagedExecutor executor;

    @Inject
    ObjectMapper objectMapper;

    /**
     * Asynchronously polls up to `attempts` times (sleeping `intervalMs` between),
     * but will break out early as soon as it finds a flight under the given budget.
     * Fires a FlightPooledEvent at that moment (or after all attempts).
     *
     * @param jsonReq         the flight request (destination, date, etc.)
     * @param jsonPoolRequest the flight budget pool request
     */
    public String findCheaperFlightAsync(Map<String, Object> jsonReq, Map<String, Object> jsonPoolRequest, String processId) {

        final FlightRequest req = objectMapper.convertValue(jsonReq, FlightRequest.class);
        final BudgetPoolRequest poolRequest = objectMapper.convertValue(jsonPoolRequest, BudgetPoolRequest.class);

        LOGGER.info("Commencing Flight Pooling for Flight {} and Budget {}", req, poolRequest);

        executor.runAsync(() -> {
            Optional<Flight> best = Optional.empty();

            for (int i = 1; i <= poolRequest.getAttempts(); i++) {
                List<Flight> flights = flightService.searchFlights(req);

                Optional<Flight> cheapestThisRound = flights.stream().min(Comparator.comparingDouble(Flight::getPrice));

                if (cheapestThisRound.isPresent()) {
                    Flight candidate = cheapestThisRound.get();
                    best = best.map(b -> b.getPrice() <= candidate.getPrice() ? b : candidate).or(() -> Optional.of(candidate));
                }

                if (best.isPresent() && best.get().getPrice() <= poolRequest.getBudget()) {
                    break;
                }

                if (i < poolRequest.getAttempts()) {
                    try {
                        Thread.sleep(poolRequest.getIntervalMs());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            CloudEvent event = new FlightPooledEvent(req, poolRequest.getBudget(), best).asCloudEvent(processId, objectMapper);
            LOGGER.info("Flight Pooling for Flight {} and Budget {}", req, best);
            InMemoryEventShared.INSTANCE.receivers().get(TravelPlannerFlow.Events.FLIGHT_POOLER_RESULT).onEvent(event);
        });

        return UUID.randomUUID().toString();
    }

}
