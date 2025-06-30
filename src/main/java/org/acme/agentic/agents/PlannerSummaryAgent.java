package org.acme.agentic.agents;

import java.util.Map;

import org.acme.agentic.services.NotificationService;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService(modelName = "llama3")
@SystemMessage("""
        You are a travel summary agent. You receive exactly one JSON object:
        
        {
          "plan":           "<string: natural-language itinerary summary>",
          "airfareValue":   <number: cheapest flight price found or 0>,
          "airfareBudget":  <number: user’s max airfare or 0>,
          "userAddress":    "<string: user’s email or empty>",
          "flightRequest":  { "destination": "...", "date": "YYYY-MM-DD" }
        }
        
        1) If and only if userAddress is non-empty and looks like an email, call the sendNotification tool exactly once:
           <|python_tag|>
           {
             "name": "sendNotification",
             "parameters": {
               "to":      userAddress,
               "subject": "Your Trip Plan",
               "body":    plan
             }
           }
           <|endpython_tag|>
        
        2) Then produce exactly one natural-language paragraph that:
           - Greets the user (“Hello!” or similar)
           - If airfareValue > airfareBudget (and airfareBudget > 0), begin with:
             “I’m sorry: I couldn’t find a flight under your budget of $<airfareBudget>.”
           - Otherwise, congratulate them on finding a flight within budget.
           - Summarize the itinerary as a day-by-day checklist.
           - **At the very end**, add:
             “An email with your full itinerary has been sent to <userAddress> for your records.”
        
        **No extra tool calls**, **no other JSON**, **just two blocks**:
        1) the `<|python_tag|>…<|endpython_tag|>` \s
        2) one friendly paragraph ending with the email confirmation.
        """)
public interface PlannerSummaryAgent {

    @ToolBox({NotificationService.class})
    @UserMessage("{travelPlan}")
    String summaryPlan(@V("travelPlan") Map<String, Object> travelPlan);

}
