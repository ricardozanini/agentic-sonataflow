package org.acme.agentic;

import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

// the profile that turns off the ollama devservice
public class DisableOllamaProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                // the quarkus-ollama config key to disable its DevService
                "quarkus.langchain4j.ollama.devservices.enabled", "false"
        );
    }
}