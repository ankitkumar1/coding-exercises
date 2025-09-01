Design and implement an in-memory task scheduler library that allows scheduling tasks to be executed at specific times or at fixed intervals. The library should provide a simple yet powerful interface for scheduling tasks, ensuring efficient management of resources and adherence to the scheduling constraints.

Functional Requirements
    1. Task Submission with Execution Time:

    2. Allow submitting a task along with a specific execution time.
        Method signature: schedule(task, time)
        Task Scheduling at Fixed Intervals:

    3. Enable scheduling tasks that should be executed at fixed intervals.
        The first execution should be immediate, and subsequent executions should happen at the specified interval after the completion of the previous task execution.
        Method signature: scheduleAtFixedInterval(task, interval) where interval is in seconds.
    
    4. Configurable Worker Threads:
        The number of worker threads used for executing tasks should be configurable.
        Efficient management of these worker threads is essential to handle the execution of scheduled tasks without overloading the system.
    
    5. Modularity and Design Patterns:
        The design and implementation of the library should be modular, following good design principles and patterns to ensure maintainability and scalability.
    
    6. No External Scheduling Libraries:
        The implementation should not rely on external or internal libraries that provide scheduling functionalities.
        Core programming language APIs and constructs should be used to build the scheduler.

=====================================================================
1. Producer–Consumer Pattern
2. Command : We send the command to be executed at scheduled time.
3. Strategy: We decide based on the API call which task oneshot or repeatable.
4. Template Method : the abstract class define the common data task, nextExecutionTime
5. Separation of Concerns (Scheduler–Executor Split). in InMemorySchedulerWithExecutorService

How it follow SOLID.
✅ S — Single Responsibility Principle
    ScheduledTask hierarchy: responsibility = task timing/rescheduling.
    Scheduler thread: responsibility = order tasks and dispatch.
    Executor pool: responsibility = run tasks.
    Each class/module has one reason to change.
✅ O — Open/Closed Principle
    Add new scheduling strategies (e.g., CronTask, RetryTask) → just extend ScheduledTask.

✅ L — Liskov Substitution Principle
    Anywhere ScheduledTask is expected, you can substitute OneShotTask or FixedIntervalTask.
    The compareTo contract, shouldReschedule, reschedule all honor the base abstraction → no broken behavior.
✅ I — Interface Segregation Principle
    API is clean: schedule(task, time), scheduleAtFixedInterval(task, interval), shutdown().
    Clients don’t depend on methods they don’t use.
    If expanded (e.g., cancel(taskId), listTasks()), you could split into smaller 
        interfaces (TaskScheduler, TaskCanceller, etc.).
✅ D — Dependency Inversion Principle
    Core logic depends on abstractions:
        Runnable (task abstraction).
        ExecutorService (execution abstraction).
    You can plug in different executors (fixed pool, cached pool, virtual threads, remote execution) without touching scheduling logic.