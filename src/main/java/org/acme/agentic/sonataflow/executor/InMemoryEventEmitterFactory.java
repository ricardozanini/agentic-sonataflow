package org.acme.agentic.sonataflow.executor;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.EventEmitter;
import org.kie.kogito.event.EventEmitterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventEmitterFactory implements EventEmitterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryEventEmitterFactory.class);

    private Map<String, EventEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public EventEmitter apply(String s) {
        LOGGER.debug("Applying event emitter {}", s);
        return new EventEmitter() {
            @Override
            public CompletionStage<Void> emit(DataEvent<?> dataEvent) {
                return null;
            }
        };
    }
}
