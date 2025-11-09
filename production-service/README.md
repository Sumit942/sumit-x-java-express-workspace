# Production Service (README)

This service is the core of the factory floor operations. Its sole responsibility is to manage the lifecycle of production jobs, from the moment they are created until they are completed, cancelled, or assigned. It also manages the factory's machine inventory.

---

## ⚙️ Core Responsibilities

* Tracks all production jobs (e.g., "make 100kg fabric").
* Manages the status of each job (`PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).
* Manages the factory's machines and their status (`AVAILABLE`, `BUSY`).
* Assigns pending jobs to available machines.
* Publishes key events to the rest of the microservice ecosystem.

---

## 🚀 Service Utilization (Use Cases)

Here is how this service is used in a real-world scenario.

### 1. Admin Task: Add a Machine

Before any work can be done, a machine must exist in the system.

* **Action:** A factory administrator adds a new machine.
* **Endpoint:** `POST /api/v1/machines`
* **Request Body:**
    ```json
    {
        "name": "Main Factory Loom - 01"
    }
    ```
* **Result:** A new machine is added to the database with an `AVAILABLE` status.

### 2. Main Production Workflow (Happy Path)

This is the standard day-to-day flow for a client's order.

#### Step 1: Create a Job
* **Context:** The `Inventory Service` has just confirmed receipt of raw materials. An external system (like the `Order Service` or an API gateway) calls this endpoint.
* **Action:** A job is created and put into the `PENDING` queue.
* **Endpoint:** `POST /api/v1/production-jobs`
* **Request Body:**
    ```json
    {
        "orderId": 123,
        "outputItemSku": "FABRIC-BLUE-COTTON",
        "inputs": [
            {
                "inputItemSku": "RAW-YARN-BLUE-A",
                "quantity": 50.5
            },
            {
                "inputItemSku": "RAW-YARN-COTTON-B",
                "quantity": 45.0
            }
        ]
    }
    ```
* **Result (Internal):** A new job is saved with `PENDING` status.
* **Event Published:** None.

#### Step 2: Start a Job
* **Context:** A factory manager, using a dashboard, assigns the `PENDING` job to an available machine.
* **Action:** The job is started.
* **Endpoint:** `POST /api/v1/production-jobs/{id}/start` (e.g., `/api/v1/production-jobs/1/start`)
* **Request Body:** (Empty)
* **Result (Internal):**
    1.  The service finds an `AVAILABLE` machine.
    2.  The job's status is updated to `IN_PROGRESS`.
    3.  The machine's status is updated to `BUSY`.
* **Event Published:** `ProductionStartedEvent` (Tells `Order Service` to update its status).

#### Step 3: Complete a Job
* **Context:** A factory worker has finished the job, weighed the final product, and is logging it.
* **Action:** The job is marked as complete.
* **Endpoint:** `POST /api/v1/production-jobs/{id}/complete` (e.g., `/api/v1/production-jobs/1/complete`)
* **Request Body:**
    ```json
    {
        "actualOutputQuantity": 92.8 
    }
    ```
* **Result (Internal):**
    1.  The job's status is updated to `COMPLETED`.
    2.  The machine's status is set back to `AVAILABLE`.
* **Event Published:** `ProductionCompletedEvent` (Tells `Inventory Service` to update stock and `Order Service` to update its status).

### 3. Exception Workflow: Cancel a Job

* **Context:** A client cancels their order while the job is still `PENDING`.
* **Action:** The `PENDING` job is cancelled.
* **Endpoint:** `POST /api/v1/production-jobs/{id}/cancel`
* **Request Body:** (Empty)
* **Result (Internal):**
    1.  The service checks if the job status is `PENDING`.
    2.  If yes, the job's status is updated to `CANCELLED`.
* **Event Published:** `JobCancelledEvent` (Tells `Order Service` and `Inventory Service` that the job is no longer active).