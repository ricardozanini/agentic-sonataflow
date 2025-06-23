package org.acme.agentic.agents;

import org.acme.agentic.services.FlightService;
import org.acme.agentic.services.HotelService;
import org.acme.agentic.services.PlannerService;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;

@RegisterAiService(modelName = "llama3")
@SystemMessage("""
    You are a travel‐planning AI with three tools. Call all of them once the user asks for the travel plan.

    Whenever you call a tool, wrap the JSON in <|python_tag|> markers:

    <|python_tag|>
    {"name":"searchFlights","parameters":{"request":{"destination":"Tokio","date":"2025-01-01"}}}
    <|endpython_tag|>

    <|python_tag|>
    {"name":"searchHotels","parameters":{"request":{"location":"Milan","maxPricePerNight":50,"nights":10}}}
    <|endpython_tag|>

    <|python_tag|>
    {"name":"buildItinerary","parameters":{"request":{"city":"Paris","interests":["games","sports"],"days":1}}}
    <|endpython_tag|>

    **Immediately after** your last tool call, output **only** a single JSON object. Nothing else. Matching **exactly** this schema:
    
    ```json
    {
      "plan": "<string: full natural-language itinerary summary>",
      "airfareValue": <number: the cheapest flight price found>,
      "airfareBudget": <number: the max flight price the user explicitly gave, or 0 if none>
    }
    ```
    Extract the user’s airfare budget by scanning their request for phrases like “max $150” or “under 200” etc.
    
    If you find one, set "airfareBudget" to that numeric value.
    
    If not, set "airfareBudget" to 0.
    
    Do NOT output any extra text, XML, or markdown—only the raw JSON object above.
    
    Example final output (after your three <|python_tag|> blocks):
    
    ```json
        {
          "plan": "Fly Lufthansa on 2025-07-10 at 22:00, stay at Berlin Inn for 3 nights under $150/night, and visit Brandenburg Gate, Museum Island, and Tempelhof Field.",
          "airfareValue": 790,
          "airfareBudget": 150
        }
    ```
    """)
public interface TravelPlannerAgent {

    @UserMessage("Today is {current_date}. Request: {userInput}")
    @ToolBox({FlightService.class, HotelService.class, PlannerService.class})
    String planTrip(@V("userInput") String userInput);

}
