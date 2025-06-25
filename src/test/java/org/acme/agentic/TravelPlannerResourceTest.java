package org.acme.agentic;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class TravelPlannerResourceTest {

    @Test
    public void testPlanTravelEndpoint() {
        // this text should include a max airfare budget so the agent can extract it
        String requirement = "I’d like to plan a trip to Berlin. \n" +
                "      I’m leaving on 2025-07-10 and want to stay 3 nights, \n" +
                "      my max is $150 per night on hotels, \n" +
                "      and I love history and great food.";

        given()
                .contentType(ContentType.TEXT)
                .body(requirement)
                .when()
                .post("/plan")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("plan", not(is(emptyString())))
                .body("airfareValue", greaterThan(0))
                .body("airfareBudget", greaterThanOrEqualTo(0));
    }
}
