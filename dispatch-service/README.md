# Dispatch Service (README)

This service is responsible for the final "money out, product out" phase of the business workflow. It manages all shipping and billing, acting as the bridge between a finished product and the client.

---

## 🏛️ Core Functionality

This service has a highly active and complex role:

1.  **Event Listener (Passive):** It listens for `ProductionCompletedEvent` from the `production-service`. When it "hears" this, it knows a product is finished and adds it to its own internal `dispatchable_stock` table, ready for shipment.

2.  **API (Active):** It provides REST endpoints for clerks to create shipments (challans) and manage invoices.

3.  **Business Logic:** It is the "owner" of billing logic. It manages the full lifecycle of an invoice (`DRAFT`, `SENT`, `PAID`, `VOIDED`) and handles the logic for partial shipments (e.g., checking available stock before allowing a shipment).

4.  **Event Publisher (Active):** When a shipment is successfully created, it publishes a `ProductDispatchedEvent` so that the `Inventory Service` can officially deduct the stock from its ledger.

5.  **Mini-Orchestrator:** When sending an invoice, this service actively calls the `Client Service` (to get an email address) and the `Notification Service` (to send the email).

---

## 🌊 Use Cases & Service Utilization

### 1. Passive Use Case: Waiting for Stock
This happens automatically in the background.

* **Event Consumed:** `ProductionCompletedEvent`
* **Action:** The `DispatchEventListener` consumes the event.
* **Result:** The service finds the `orderId` and `outputItemSku` from the event and adds the `actualOutputQuantity` (e.g., `+95.0 Kg`) to its internal `dispatchable_stock` table. **The product is now officially ready to be shipped.**

---

### 2. Active Use Cases (API Endpoints)
These are the manual actions a dispatch clerk would perform.

#### Use Case 1: Create a Shipment (Challan)
* **Context:** A clerk is ready to ship the 95kg of fabric that is now available.
* **Endpoint:** `POST /api/v1/shipments`
* **Request Body:**
    ```json
    {
        "orderId": 123,
        "clientId": 1,
        "shippingAddress": "123 Main St, New Delhi, 110001",
        "items": [
            {
                "itemSku": "FABRIC-BLUE-COTTON",
                "quantity": 95.0
            }
        ]
    }
    ```
* **Result:**
    1.  The service validates this request against the `dispatchable_stock` table.
    2.  A `Shipment` record is created (the challan).
    3.  The `availableQuantity` in `dispatchable_stock` is reduced (e.g., `95.0 - 95.0 = 0`).
    4.  A `ProductDispatchedEvent` is **published** to Kafka, telling the `Inventory Service` to update its main ledger.

#### Use Case 2: Create an Invoice
* **Context:** After creating a shipment, the clerk needs to generate a bill.
* **Endpoint:** `POST /api/v1/invoices`
* **Request Body:**
    ```json
    {
        "shipmentId": 1, 
        "amount": 10000.0
    }
    ```
* **Result:** An `Invoice` record is created with the status **`DRAFT`**.

#### Use Case 3: Send the Invoice
* **Context:** The clerk reviews the `DRAFT` invoice and clicks "Send." This is a complex action.
* **Endpoint:** `POST /api/v1/invoices/{id}/send` (e.g., `/api/v1/invoices/1/send`)
* **Result (Saga):**
    1.  The service checks if the invoice status is `DRAFT`.
    2.  It calls the `Client Service` API (`GET /api/v1/clients/1`) to fetch the client's email.
    3.  It calls the `Notification Service` API (`POST /api/v1/notifications/send`) to send the email.
    4.  It updates its local invoice status from `DRAFT` to **`SENT`**.

#### Use Case 4: Void an Invoice
* **Context:** An error was found on a `SENT` invoice. It cannot be deleted, so it must be voided.
* **Endpoint:** `POST /api/v1/invoices/{id}/void`
* **Result:** The invoice status is updated from `SENT` to **`VOIDED`**.

---

### 📈 Invoice Lifecycle
This service is the sole owner of the invoice status, which flows as follows:

`DRAFT` -> `SENT` -> `PAID` (or `VOIDED`)