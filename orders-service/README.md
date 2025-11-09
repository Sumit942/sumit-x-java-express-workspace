# Order Service (README)

This is the central "brain" or "state machine" for the entire textile manufacturing workflow. Its primary responsibility is to create, manage, and track the high-level status of a client's order from its creation to its completion.

---

## 🏛️ Core Functionality

This service has two distinct jobs that follow SOLID principles:

1.  **API (Active Role):** It exposes RESTful endpoints for external users (or other services) to **create**, **view**, and **cancel** orders. This is the "command" part of the service.
2.  **Event Listener (Passive Role):** It listens to various Kafka topics for events published by other services (like `Production Service`, `Inventory Service`, etc.). It consumes these events to **automatically update** the order's status, acting as the single source of truth for the workflow.

---

## 🌊 Use Cases & Service Utilization

### 1. Active Use Cases (API Endpoints)

These are the actions you take *on* this service.

#### Use Case 1: Create a New Order
This is the **first step** in the entire business process.
* **Action:** A user creates a new order for a client.
* **Endpoint:** `POST /api/v1/orders`
* **Request Body:**
    ```json
    {
        "clientId": 1,
        "orderDescription": "100kg of blue cotton fabric, design #4"
    }
    ```
* **Result:** A new order is created in the database with the initial status of **`AWAITING_MATERIAL`**.

#### Use Case 2: Check an Order's Status
* **Action:** A user or dashboard needs to check the current status of a job.
* **Endpoint:** `GET /api/v1/orders/{id}` (e.g., `/api/v1/orders/1`)
* **Result:** The service returns the full order details, including its current status (e.g., `"status": "IN_PRODUCTION"`).

#### Use Case 3: Cancel an Order
* **Action:** A client calls to cancel their order.
* **Endpoint:** `POST /api/v1/orders/{id}/cancel` (e.g., `/api/v1/orders/1/cancel`)
* **Result:**
    1.  The service updates its local database status to **`CANCELLED`**.
    2.  It publishes an **`OrderCancelledEvent`** to a Kafka topic, so other services (like `Production Service`) know to stop their work.

### 2. Passive Use Cases (Event-Driven Updates)

This service is always listening. You **do not** call an API for these actions; they happen automatically.

#### Scenario 1: Production Starts
* **Event:** `Production Service` publishes a `ProductionStartedEvent`.
* **Result:** The `OrderEventListener` consumes this, finds the order, and updates its status from `AWAITING_PRODUCTION` to **`IN_PRODUCTION`**.

#### Scenario 2: Production Finishes
* **Event:** `Production Service` publishes a `ProductionCompletedEvent`.
* **Result:** The `OrderEventListener` consumes this and updates the order status from `IN_PRODUCTION` to **`PRODUCTION_COMPLETE`**.

#### Scenario 3: A Production Job is Cancelled
* **Event:** `Production Service` publishes a `JobCancelledEvent` (e.g., because a machine broke).
* **Result:** The `OrderEventListener` consumes this and sets the status back to **`AWAITING_PRODUCTION`**, so the job can be re-queued.

---

## 📈 Order Status Lifecycle

This service manages the order's progression through these states:

1.  **`AWAITING_MATERIAL`** (Initial state upon creation)
    * *Waits for `Inventory Service` to publish `RAW_MATERIAL_RECEIVED`*
2.  **`AWAITING_PRODUCTION`** (Material is in-house)
    * *Waits for `Production Service` to publish `PRODUCTION_STARTED`*
3.  **`IN_PRODUCTION`** (Job is on a machine)
    * *Waits for `Production Service` to publish `PRODUCTION_COMPLETED`*
4.  **`PRODUCTION_COMPLETE`** (Job is finished, ready for dispatch)
    * *Waits for `Dispatch Service` to publish `PRODUCT_DISPATCHED`*
5.  **`PARTIALLY_DISPATCHED`** / **`COMPLETED`** (Order is shipped)
6.  **`CANCELLED`** (If the `POST .../cancel` endpoint is called)