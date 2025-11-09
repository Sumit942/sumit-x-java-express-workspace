# Inventory Service (README)

This service is the central, immutable **ledger** for the entire manufacturing application. Its sole responsibility is to track all physical stock—both raw materials and finished goods—in a reliable and auditable way.

It is designed as an **append-only ledger**, meaning stock levels are never updated or deleted. Instead, transactions are added to show movement, and the current stock is a real-time calculation of all transactions for an item.

---

## 🏛️ Core Functionality

This service has two distinct roles:

1.  **Active Role (API):** It provides RESTful endpoints for manual operations. This includes adding new items to the catalog (SKUs), manually receiving raw materials from a client, adjusting stock, and querying current stock levels.
2.  **Passive Role (Event Listener):** It listens to Kafka topics for events from other services. When it "hears" that production is finished or a product is shipped, it automatically creates the corresponding ledger entries to keep stock levels in sync.

---

## 🌊 Use Cases & Service Utilization

### 1. Prerequisite: Add an Item to the Catalog

Before you can track stock, the item's SKU must exist.

* **Action:** Define "Raw Cotton Yarn" in the system.
* **Endpoint:** `POST /api/v1/inventory/items`
* **Body:**
    ```json
    {
        "sku": "RAW-YARN-COTTON",
        "name": "Raw Cotton Yarn",
        "type": "RAW_MATERIAL",
        "unit": "Kg"
    }
    ```

---

### 2. Active Use Cases (API Endpoints)

These are the operations you manually perform on the service.

#### Use Case 1: Receive Raw Material (Workflow Start)
This is the **first major step** of the entire business workflow.

* **Action:** A worker receives 100Kg of cotton for an order.
* **Endpoint:** `POST /api/v1/inventory/inward`
* **Body:**
    ```json
    {
        "orderId": 123,
        "clientId": 1,
        "sku": "RAW-YARN-COTTON",
        "quantity": 100.0
    }
    ```
* **Result:**
    1.  **Internal:** A ledger entry is created: `+100.0 Kg` for `RAW-YARN-COTTON` with type `INWARD_RAW`.
    2.  **Event Published:** A `RawMaterialReceivedEvent` is published to Kafka, telling the `Order Service` it can move to the `AWAITING_PRODUCTION` state.

#### Use Case 2: Check Current Stock
* **Action:** Check the real-time stock for an item.
* **Endpoint:** `GET /api/v1/inventory/stock/RAW-YARN-COTTON`
* **Result:** The service calculates `SUM(quantity)` for all ledger entries and returns the current level.
    ```json
    {
        "itemId": 1,
        "sku": "RAW-YARN-COTTON",
        "currentStock": 100.0,
        "unit": "Kg"
    }
    ```

#### Use Case 3: Audit an Item's History
* **Action:** Get a full, unchangeable history of all movements for an item.
* **Endpoint:** `GET /api/v1/inventory/history/RAW-YARN-COTTON`
* **Result:** A list of all `InventoryLedger` entries for that SKU.

#### Use Case 4: Manual Stock Adjustment
* **Action:** An audit finds 1Kg of yarn was damaged and must be written off.
* **Endpoint:** `POST /api/v1/inventory/adjust`
* **Body:**
    ```json
    {
        "sku": "RAW-YARN-COTTON",
        "quantity": -1.0,
        "reason": "Water damage found during audit"
    }
    ```
* **Result:** A ledger entry is created: `-1.0 Kg` with type `ADJUSTMENT`.

---

### 3. Passive Use Cases (Event-Driven Updates)

This service is always listening. You **do not** call an API for these actions.

#### Scenario 1: Production is Completed
* **Event Consumed:** `ProductionCompletedEvent` (from `production-service`)
* **Result:** The `InventoryEventListener` automatically creates **two** new ledger entries for Order 123:
    1.  **Consumption:** `-100.0 Kg` of `RAW-YARN-COTTON` (type `PRODUCTION_CONSUMED`).
    2.  **Creation:** `+95.0 Kg` of `FABRIC-BLUE-COTTON` (type `PRODUCTION_COMPLETED`).

#### Scenario 2: Product is Shipped
* **Event Consumed:** `ProductDispatchedEvent` (from `dispatch-service`)
* **Result:** The `InventoryEventListener` creates **one** new ledger entry for Order 123:
    1.  **Shipment:** `-95.0 Kg` of `FABRIC-BLUE-COTTON` (type `OUTWARD_DISPATCH`).

At the end of this flow, the stock for both items related to Order 123 is `0`, and the ledger contains a perfect, auditable trail of its entire lifecycle.