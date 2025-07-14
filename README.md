# agentic-sonataflow

A sample Quarkus application demonstrating **agentic AI** workflows using **LangChain4j**, **Ollama** (local LLM), and **SonataFlow** (CNCF Serverless Workflow Fluent API). It wires together conversational AI agents, mock services, and serverless workflows to build end-to-end demos—such as a travel planner that decomposes user requests, invokes tool-using agents, pools flight prices, and notifies users.

---

## 📖 Overview

- **TravelPlannerAgent**: a LangChain4j AI service (Quarkus-managed) that plans trips by calling three tools—`searchFlights`, `searchHotels`, `buildItinerary`—in one shot and returns a JSON summary.  
- **PlannerSummaryAgent**: post-planner AI agent that formats a final natural-language summary and uses a notification tool to email users.  
- **BudgetFlightPooler**: an async CDI bean that polls `FlightService` up to N attempts, emits a CDI event when a flight under budget is found, and optionally triggers workflow actions.  
- **SonataFlow Workflows**: model workflows (e.g., a flight-price-watcher loop) with the fluent API and inject the AI agents and services as steps.  

---

## 🚀 Prerequisites & Local LLM

### Container engine (for Ollama DevServices)

- **macOS (Colima)**  
  ```bash
  colima start --network-address --memory 18 --cpu 4
  ```
- **Linux (Podman)**  
  ```bash
  podman machine init --memory 18 --cpus 4
  podman machine start
  ```
- **Linux/Windows (Docker Desktop)**  
  Ensure Docker Desktop is running with at least 16 GB RAM allocated.

### Build & Run

1. **Clone**  
   ```bash
   git clone https://github.com/ricardozanini/agentic-sonataflow.git
   cd agentic-sonataflow
   ```
2. **Build & Test**  
   ```bash
   mvn clean verify
   ```
3. **Dev Mode** (auto-reload, Ollama DevServices)  
   ```bash
   mvn quarkus:dev
   ```
4. **Invoke the Planner**  
   ```bash
   curl -X POST http://localhost:8080/plan      -H 'Content-Type: text/plain'      -d "I’d like to plan a trip to Berlin on 2025-07-10 for 3 nights, max airfare $150/night, I love history and food."
   ```

---

## 🏗️ Project Structure

```
.
├── pom.xml
├── src
│   ├── main
│   │   ├── java/org/acme/agentic
│   │   │   ├── agents           # @RegisterAiService interfaces
│   │   │   ├── model            # POJOs: Flight, Hotel, TravelRequest, etc.
│   │   │   ├── services         # FlightService, HotelService, PlannerService & stubs
│   │   │   ├── workflows        # SonataFlow fluent-API workflows (TravelPlannerFlow)
│   │   │   └── TravelPlannerResource.java
│   │   └── resources
│   │       └── mock-data        # stub JSON: flights.json, hotels.json, itineraries.json
│   └── test
│       ├── java/...             # QuarkusTest + LangChain4j scorer tests
│       └── resources
│           └── samples.yaml     # test cases for TravelPlannerAgent & PlannerSummaryAgent
└── README.md
```

---

## 🔄 SonataFlow Workflows

The **flight-price-watcher** workflow in [src/main/java/org/acme/agentic/workflows/TravelPlannerFlow.java](https://github.com/ricardozanini/agentic-sonataflow/blob/main/src/main/java/org/acme/agentic/workflows/TravelPlannerFlow.java):

1. Calls the travel planner agent (`planTrip`) once.  
2. Checks whether the fare is within budget; if yes, calls the summary agent.  
3. Otherwise, notifies the user that pooling begins, invokes the async pooler, waits for its event, then calls the summary agent again.

---

## 🧪 Testing

- **LangChain4j Scorer**: JUnit 5 extension to run agent tests against `samples.yaml`.  
- **QuarkusTest** + **Mockito**: for service beans (e.g., stubbing `FlightService`) and CDI event observers.  
- **Rest-Assured**: for end-to-end REST tests of `/plan` endpoint.  
- **DisableOllamaProfile**: a custom Quarkus Test Profile to disable DevServices and speed up tests.  
