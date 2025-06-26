package org.acme.agentic.services;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class TestFlightListener {
    private CountDownLatch latch = new CountDownLatch(1);
    private BudgetFlightPooler.FlightPooledEvent lastEvent;

    public void onPooled(@Observes BudgetFlightPooler.FlightPooledEvent e) {
        this.lastEvent = e;
        latch.countDown();
    }

    public void reset() {
        latch = new CountDownLatch(1);
        lastEvent = null;
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    public BudgetFlightPooler.FlightPooledEvent getLastEvent() {
        return lastEvent;
    }
}