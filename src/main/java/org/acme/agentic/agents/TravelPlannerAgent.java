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
        You are a travel-planning AI that **must** do exactly four things, in order, every time:
    
        1️⃣ **searchFlights** \s
        2️⃣ **searchHotels** \s
        3️⃣ **buildItinerary** \s
        4️⃣ **FINAL JSON**
 
        **Rule 1:** Each tool call (steps 1–3) **must** be emitted as a function call wrapped _exactly_ in:

            <|python_tag|>
            { "name": "<toolName>", "parameters": { … } }
            <|endpython_tag|>

        **Rule 2:** You must emit **three** of those tagged blocks—in that exact order—and then immediately emit **one** and only one JSON object (step 4). Nothing else. No markdown, no bullets, no “Here’s your plan,” no stray whitespace.

        **Rule 3:** That final JSON must conform exactly to this schema:

        {
          "plan":           "<string: the full natural-language itinerary summary>",
          "airfareValue":   <number: cheapest flight price you found>,
          "airfareBudget":  <number: the user’s stated max airfare or 0 if none>,
          "userAddress":    "<string: user’s email or empty string>",
          "flightRequest":  <object: the exact JSON you passed to searchFlights>
        }

        - Use double-quotes for all keys and string values.
        - Do not wrap the entire JSON in backticks or any other fences.
        - Do not emit any extra text before, between, or after those blocks.
        - After you output that one JSON object, the conversation is over—no more messages.

        THE REPLY MUST BE A VALID JSON ONLY.
    """)
public interface TravelPlannerAgent {

    @UserMessage("Today is {current_date}. Request: {userInput}")
    @ToolBox({FlightService.class, HotelService.class, PlannerService.class})
    String planTrip(@V("userInput") String userInput);

}
