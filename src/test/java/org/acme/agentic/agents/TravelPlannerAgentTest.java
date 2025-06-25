package org.acme.agentic.agents;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkiverse.langchain4j.scorer.junit5.AiScorer;
import io.quarkiverse.langchain4j.scorer.junit5.SampleLocation;
import io.quarkiverse.langchain4j.scorer.junit5.ScorerConfiguration;
import io.quarkiverse.langchain4j.testing.scorer.EvaluationReport;
import io.quarkiverse.langchain4j.testing.scorer.Parameters;
import io.quarkiverse.langchain4j.testing.scorer.Samples;
import io.quarkiverse.langchain4j.testing.scorer.Scorer;
import io.quarkiverse.langchain4j.testing.scorer.similarity.SemanticSimilarityStrategy;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@AiScorer
public class TravelPlannerAgentTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TravelPlannerAgentTest.class);

    @Inject
    AiServiceEvaluation aiServiceEvaluation;

    @Test
    void testTravelPlanner(@ScorerConfiguration(concurrency = 2) Scorer scorer,
                           @SampleLocation("src/test/resources/samples.yaml") Samples<String> samples) {
        EvaluationReport<String> report = scorer.evaluate(
                samples,
                aiServiceEvaluation,
                new SemanticSimilarityStrategy(0.8)
        );
        LOGGER.info("Evaluation report is: \n {}", report.evaluations());
        assertThat(report.score()).isGreaterThanOrEqualTo(70.0);
    }

    @Singleton
    public static class AiServiceEvaluation implements Function<Parameters, String> {

        @Inject
        TravelPlannerAgent travelPlannerAgent;

        @ActivateRequestContext
        @Override
        public String apply(Parameters params) {
            return travelPlannerAgent.planTrip(params.get(0));
        }
    }

}
