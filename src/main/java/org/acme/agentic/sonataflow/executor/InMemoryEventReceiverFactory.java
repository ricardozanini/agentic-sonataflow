package org.acme.agentic.sonataflow.executor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kie.kogito.event.EventReceiver;
import org.kie.kogito.event.EventReceiverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventReceiverFactory implements EventReceiverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryEventReceiverFactory.class);

    private final Map<String, InMemoryEventReceiver> receivers = new ConcurrentHashMap<>();

    @Override
    public EventReceiver apply(String s) {
        LOGGER.debug("Applying event receiver {}", s);
        return receivers.computeIfAbsent(s, k -> new InMemoryEventReceiver());
    }
}
