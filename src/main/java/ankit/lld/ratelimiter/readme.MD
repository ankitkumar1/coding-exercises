Why we need separet RateLimiter for each user?
Rate limiting is almost always per key (per user, per API key, per IP, per service).
So the state (counters, bucket fill level, timestamps) must be independent for each user.
Each user has their own state machine.
Use one instance per user (or per key), because rate limiting depends on user-specific state.

Why Eviction Is Needed
Each RateLimiter holds state (tokens, water, timestamps).
If you keep a limiter forever, inactive users bloat memory.
At scale (millions of users), memory leaks → OOM risk.
=> Add a scheduled job which keep checking the last access time for each limiter and remove that from the registry.
ScheduledExecutorService : Maintain lastAccessTime inside each limiter.
Run a periodic cleanup job that removes stale entries.

