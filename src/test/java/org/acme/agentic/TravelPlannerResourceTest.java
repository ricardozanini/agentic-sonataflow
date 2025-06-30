package org.acme.agentic;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class TravelPlannerResourceTest {

    @Test
    public void testPlanTravelAboveBudgetEndpoint() {
        // this text should include a max airfare budget so the agent can extract it
        String requirement = "I’d like to plan a trip to Berlin. \n" +
                "      I’m leaving on 2025-07-10 and want to stay 3 nights, \n" +
                "      my max is $150. For airfare, my max budget is $100 \n" +
                "      and I love history and great food. If you need to contact me later, my email is luke@love.ia";

        given()
                .contentType(ContentType.TEXT)
                .body(requirement)
                .when()
                .post("/plan")
                .then()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .body(not(is(emptyString())));
    }

    @Test
    public void testPlanTravelBelowBudgetEndpoint() {
        // this text should include a max airfare budget so the agent can extract it
        String requirement = "I’d like to plan a trip to Berlin. \n" +
                "      I’m leaving on 2025-07-10 and want to stay 3 nights, \n" +
                "      my max is $1500. For airfare, my max budget is $3000 \n" +
                "      and I love history and great food. If you need to contact me later, my email is luke@love.ia";

        given()
                .contentType(ContentType.TEXT)
                .body(requirement)
                .when()
                .post("/plan")
                .then()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .body(not(is(emptyString())));
    }
}
