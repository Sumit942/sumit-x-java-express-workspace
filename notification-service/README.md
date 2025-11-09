# Notification Service (README)

This is a dedicated, asynchronous microservice responsible for handling all outbound communications for the application (e.g., emails, SMS).

Its core design principle is **asynchronicity**. When other services (like the `Dispatch Service`) need to send an email, they make a single, non-blocking API call. This service immediately accepts the request, saves it to a database, and returns a `202 ACCEPTED` response. A separate background thread then processes the request, ensuring that the calling service is not blocked by network delays from an SMTP server.

---

## 🏛️ Core Functionality

* **Accepts Requests:** Provides a single API endpoint (`POST /api/v1/notifications/send`) to receive notification requests.
* **Logs Everything:** Immediately saves every request to a `notification_log` table with a `PENDING` status.
* **Asynchronous Processing:** Uses Spring's `@Async` to process the `PENDING` job in a separate thread.
* **Sends Notifications:** Calls an (internal) email/SMS sender to dispatch the message.
* **Updates Status:** Updates the log entry to `SENT` or `FAILED` based on the outcome.
* **Auditing:** Provides API endpoints to check the status of a specific notification log.

---

## 🌊 Use Case: Sending an Invoice

This is the primary use case for this service.

1.  The `Dispatch Service` needs to send an invoice.
2.  It makes a `POST` request to `http://localhost:8086/api/v1/notifications/send` with the client's email, subject, and body.
3.  The `Notification Service` **immediately** validates the request, saves it to the database with `logId: 1` and `status: PENDING`, and returns a `202 ACCEPTED` response to the `Dispatch Service`.
4.  The `Dispatch Service` receives this "OK" and moves on with its own work, its task complete.
5.  A moment later, a background thread (`@Async`) in the `Notification Service` picks up `logId: 1`. It connects to the mock SMTP server (simulating a 2-second delay).
6.  The send succeeds. The background thread updates the `logId: 1` row in the database, setting `status: SENT` and `sent_at: ...`.

---

## 📋 API Endpoints

### 1. Send a Notification
This is the main endpoint used by other microservices.

* `POST /api/v1/notifications/send`
* **Request Body:**
    ```json
    {
        "clientId": 1,
        "channel": "EMAIL",
        "recipient": "client-email@example.com",
        "subject": "Invoice Ready: INV-123-ABCD",
        "body": "Dear Client,\n\nYour invoice is attached."
    }
    ```
* **Success Response (Synchronous):** `202 ACCEPTED`
    ```json
    {
        "logId": 1,
        "status": "PENDING",
        "message": "Notification request accepted and is being processed."
    }
    ```

### 2. Check a Notification's Status
Used for auditing or to check if a notification was sent successfully.

* `GET /api/v1/notifications/{id}`
* **Success Response (after a few seconds):** `200 OK`
    ```json
    {
        "logId": 1,
        "clientId": 1,
        "channel": "EMAIL",
        "recipient": "client-email@example.com",
        "subject": "Invoice Ready: INV-123-ABCD",
        "body": "Dear Client,\n\nYour invoice is attached.",
        "status": "SENT",
        "sentAt": "2025-11-09T22:30:05.123456",
        "createdAt": "2025-11-09T22:30:02.987654"
    }
    ```

### 3. Get All Logs for a Client
* `GET /api/v1/notifications/by-client/{clientId}`
* **Success Response:** `200 OK` (Returns a list of all logs for that client).