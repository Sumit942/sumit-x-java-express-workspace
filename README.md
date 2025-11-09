# Textile Manufacturing ERP (Event-Driven Microservices)

This project is a back-end application for a textile manufacturing company. It is built using a modern, event-driven microservice architecture to manage the entire business workflow, from receiving raw client materials to processing, billing, and dispatching the finished products.

---

## 🏛️ Core Architecture

The system is designed as a set of independent, loosely-coupled microservices that communicate asynchronously using a **Message Bus** (like Kafka or RabbitMQ). This event-driven approach ensures high resilience, scalability, and maintainability.

The workflow is managed using the **Saga Pattern (Choreography)**, where each service performs its task and then publishes an event. Other services listen for these events to trigger their own part of the process, without a central orchestrator.



---

## 📦 Services Overview

The application is broken down into 6 (six) core microservices, each with a single responsibility (SRP).

* **`Client Service`**
    * **Responsibility:** Manages all client (company/organization) data.
    * **Function:** Acts as the CRM, storing contact information, billing addresses, GST numbers, and bank details.
    * **Port:** `9006` (example)

* **`Order Service`**
    * **Responsibility:** Manages the overall state of a client's order.
    * **Function:** This is the "source of truth" for a job's status (e.g., `AWAITING_MATERIAL`, `IN_PRODUCTION`, `COMPLETED`, `CANCELLED`). It listens to events from all other services to update its state.
    * **Port:** `9002` (example)

* **`Inventory Service`**
    * **Responsibility:** Manages the real-time financial ledger of all stock.
    * **Function:** This is an append-only ledger. It tracks raw materials coming in (`+100kg Yarn`), materials consumed by production (`-100kg Yarn`), finished goods created (`+95kg Fabric`), and products shipped (`-95kg Fabric`).
    * **Port:** `9001` (example)

* **`Production Service`**
    * **Responsibility:** Manages all factory floor operations.
    * **Function:** Tracks machine availability (`AVAILABLE`, `BUSY`), manages the queue of production jobs, and handles the start, completion, and cancellation of jobs.
    * **Port:** `9003` (example)

* **`Dispatch Service`**
    * **Responsibility:** Manages all shipping and billing.
    * **Function:** Creates shipments (challans) for finished goods, handles partial dispatches, and generates invoices. It also manages the invoice lifecycle (`DRAFT`, `SENT`, `PAID`, `VOIDED`).
    * **Port:** `9005` (example)

* **`Notification Service`**
    * **Responsibility:** Sends all communications to clients.
    * **Function:** A simple, dedicated service that receives requests from other services to send emails (e.g., "Order Completed," "Invoice Sent"). It runs asynchronously to avoid blocking other services.
    * **Port:** `9004` (example)

---

## 🛠️ Technology Stack

* **Java 17+**
* **Spring Boot 3+**
* **Spring Data JPA (Hibernate):** For database interaction.
* **Spring Web:** For creating RESTful APIs.
* **Database:** MySQL / PostgreSQL (Per-service database)
* **Maven:** For dependency management and build.
* **Lombok:** To reduce boilerplate code.
* **MapStruct:** For high-performance DTO-to-Entity mapping.
* **Message Bus:** Apache Kafka or RabbitMQ (for event-driven communication).

---

## ⚙️ Key Design Patterns

* **SOLID Principles:** The entire architecture is built on SOLID, especially the **Single Responsibility Principle**.
* **Database-per-Service:** Each microservice owns its own database schema and is the only service allowed to access it directly.
* **DTO (Data Transfer Object):** We use DTOs to separate our internal database models (`@Entity`) from our external API contracts.
* **Repository-Service-Controller Layer:** Standard layered architecture for maintainability and testability.
* **`@RestControllerAdvice`:** A global exception handler in each service provides consistent JSON error responses.
* **Saga Pattern (Choreography):** Used to manage distributed transactions (the order workflow) without a single point of failure.

---

## 🌊 Workflow Example (The Saga)

Here is a step-by-step flow of a typical order:

1.  **`Inventory Service`**: A worker logs raw material: `POST /api/v1/inventory/inward`.
    * **Event Published:** `RAW_MATERIAL_RECEIVED`
2.  **`Order Service`** (Listens): Hears the event, updates the order status to `AWAITING_PRODUCTION`.
3.  **`Production Service`** (Listens): Hears the event, adds the job to the "Pending Jobs" queue.
4.  **`Production Service`**: A manager starts the job: `POST /api/v1/production-jobs/{id}/start`.
    * Finds an available machine, sets its status to `BUSY`.
    * Sets the job status to `IN_PROGRESS`.
    * **Event Published:** `PRODUCTION_STARTED`
5.  **`Order Service`** (Listens): Hears the event, updates the order status to `IN_PRODUCTION`.
6.  **`Production Service`**: A worker finishes the job: `POST /api/v1/production-jobs/{id}/complete`.
    * Sets the job status to `COMPLETED`.
    * Sets the machine status to `AVAILABLE`.
    * **Event Published:** `PRODUCTION_COMPLETED`
7.  **`Inventory Service`** (Listens): Hears the event, updates its ledger:
    * *Decrements* raw material stock (e.g., `-100kg Yarn`).
    * *Increments* finished goods stock (e.g., `+95kg Fabric`).
8.  **`Order Service`** (Listens): Hears the event, updates the order status to `PRODUCTION_COMPLETE`.
9.  **`Notification Service`** (Listens): Hears the event, sends an "Order Ready" email to the client.
10. **`Dispatch Service`**: A clerk creates a shipment: `POST /api/v1/shipments`.
    * Creates a challan.
    * **Event Published:** `PRODUCT_DISPATCHED`
11. **`Inventory Service`** (Listens): Hears the event, *decrements* the finished goods stock (e.g., `-95kg Fabric`).
12. **`Order Service`** (Listens): Hears the event, updates the order status to `COMPLETED`.
13. **`Dispatch Service`**: A clerk sends the invoice: `POST /api/v1/invoices/{id}/send`.
    * Calls **`Client Service`** to get the client's email.
    * Calls **`Notification Service`** to send the invoice email.
    * Updates the invoice status from `DRAFT` to `SENT`.

---

## 🚀 How to Run a Service

To run any of the microservices (e.g., `client-service`):

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/Sumit942/sumit-x-java-express-workspace.git](https://github.com/Sumit942/sumit-x-java-express-workspace.git)
    cd sumit-x-java-express-workspace/client-service
    ```
2.  **Set up the Database:**
    * Create a new database (e.g., `client_db`) in MySQL or PostgreSQL.
3.  **Configure the Service:**
    * Open `src/main/resources/application.yaml`.
    * Update the `spring.datasource.url`, `username`, and `password`.
    * Set the server port (e.g., `server.port=9006`).
    * Add connection URLs for other services (if needed) and the Message Bus.
4.  **Build the Project:**
    * This is a critical step that runs MapStruct.
    ```bash
    mvn clean install
    ```
5.  **Run the Application:**
    ```bash
    java -jar target/client-service-0.0.1-SNAPSHOT.jar
    ```
6.  Repeat these steps for all other services.

---

## 📋 API Design

The API follows RESTful principles.

* **Create:** `POST /api/v1/{resource}`
* **Read (One):** `GET /api/v1/{resource}/{id}`
* **Read (All):** `GET /api/v1/{resource}`
* **Update:** `PUT /api/v1/{resource}/{id}`
* **Delete:** `DELETE /api/v1/{resource}/{id}`

For complex **business actions**, we use `POST` to an action endpoint to avoid breaking REST principles.
* `POST /api/v1/orders/{id}/cancel`
* `POST /api/v1/invoices/{id}/void`
* `POST /api/v1/production-jobs/{id}/complete`