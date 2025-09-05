# 🔹 Design Patterns in the In-Memory Notification System

### 1. **Strategy Pattern** (for NotificationChannel)

* **Where:** `NotificationChannel` interface + `EmailChannel`, `SMSChannel`, `PushChannel`.
* **Why:** Encapsulates the algorithm for sending a notification.
* **Benefit:** Adding a new channel (e.g., WhatsApp, Slack) requires only a new class, no changes to existing code (OCP).

---

### 2. **Producer–Consumer Pattern** (via BlockingQueue + Dispatcher)

* **Where:** `NotificationService` enqueues, `NotificationDispatcher` consumes.
* **Why:** Decouples producers (clients calling API) from consumers (workers processing).
* **Benefit:** Async processing, smoother load handling, natural backpressure.

---

### 3. **Command Pattern** (implicit in NotificationRequest)

* **Where:** `NotificationRequest` is a self-contained object holding all details of an action (send this message to this recipient via this channel).
* **Why:** Treats each notification as a command to be executed later.
* **Benefit:** Easy to enqueue, retry, or even persist to disk for replay.

---

### 4. **Observer Pattern** (optional extension: status updates)

* **Where (potential):** Listeners could subscribe to status changes (e.g., loggers, metrics, auditing).
* **Why:** Decouples notification processing from status tracking.
* **Benefit:** Pluggable observers (like Prometheus metrics, Splunk logs) without modifying core logic.

---

### 5. **Singleton / Service Locator** (practical system-wide)

* **Where:** `NotificationService` often acts as a singleton in app context.
* **Why:** Central orchestrator; you don’t want multiple queues/services floating around.

---

### 6. **Template Method** (hidden inside Dispatcher retry logic)

* **Where:** `deliver(req, attempt)` → defines skeleton: pick channel, attempt send, retry if needed.
* **Why:** Retry workflow is a “template” with steps that may vary (e.g., exponential backoff).

---

### 7. **Facade Pattern** (NotificationService API)

* **Where:** `NotificationService.send()` gives a simple interface to clients.
* **Why:** Hides complexity (queueing, retrying, threading, channel lookup).
* **Benefit:** Client just says *“send this message”*, doesn’t care how it’s dispatched.

---

### 8. **Dependency Injection / Inversion of Control**

* **Where:** `NotificationDispatcher` depends on `NotificationChannel` interface, not concrete implementations.
* **Why:** Enables plugging in mock channels in tests, or real channels in prod.
* **Benefit:** Loose coupling, high testability.

