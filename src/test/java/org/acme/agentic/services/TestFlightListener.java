package org.acme.agentic.services;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.jackson.JsonCloudEventData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class TestFlightListener {

    @Inject
    ObjectMapper objectMapper;

    private CountDownLatch latch = new CountDownLatch(1);
    private FlightPooledEvent lastEvent;

    public void onPooled(@Observes CloudEvent event) throws JsonProcessingException {
        JsonCloudEventData ced = (JsonCloudEventData) event.getData();
        if (ced != null) {
            this.lastEvent = objectMapper.treeToValue(ced.getNode(), FlightPooledEvent.class);
        } else {
            throw new IllegalStateException("Event Data not a JsonCloudEventData: " + event);
        }

        latch.countDown();
    }

    public void reset() {
        latch = new CountDownLatch(1);
        lastEvent = null;
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    public FlightPooledEvent getLastEvent() {
        return lastEvent;
    }
}