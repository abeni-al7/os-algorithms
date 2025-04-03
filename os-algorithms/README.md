# Operating Systems Algorithms for Mutual Exclusion

This repository contains implementations of several classic algorithms used in operating systems to solve the critical section problem, ensuring mutual exclusion between concurrent processes or threads.

## Algorithms Implemented

1.  **Disabling Interrupts**: A simple approach where a process disables interrupts before entering a critical section and re-enables them after exiting.
2.  **Lock Variables**: Uses a shared lock variable to control access to the critical section.
3.  **Strict Alternation**: A synchronization mechanism where two processes take turns accessing the critical section.
4.  **Peterson's Algorithm (Mutual Exclusion)**: A classic software-based solution for mutual exclusion between two processes.
5.  **Semaphores**: A synchronization primitive that uses a counter to control access to shared resources.
6.  **Monitors**: A higher-level synchronization construct that encapsulates shared data and the operations that access it.
7.  **Message Passing**: A communication mechanism where processes exchange messages to coordinate access to shared resources.

## Implementation Details

### 1. Disabling Interrupts

*   **File**: [DisablingInterrupts.java](DisablingInterrupts.java)
*   **Description**: This approach prevents context switching by disabling interrupts, ensuring that the current process can execute its critical section without interference.
*   **Implementation**:
    *   The `enterCriticalSection()` method sets `interruptsEnabled` to `false`, simulating the disabling of interrupts.
    *   The `exitCriticalSection()` method sets `interruptsEnabled` to `true`, re-enabling interrupts.
    *   The `areInterruptsEnabled()` method returns the current status of interrupts.
*   **Note**: This method is generally not suitable for user-level programs as it requires OS-level privileges.

### 2. Lock Variables

*   **File**: [LockVariables.java](LockVariables.java)
*   **Description**: This method uses an `AtomicBoolean` to represent a lock. A thread tries to acquire the lock using `compareAndSet`.
*   **Implementation**:
    *   The `lock()` method continuously tries to set the lock to `true` using `compareAndSet` until it succeeds.
    *   The `unlock()` method sets the lock back to `false`.
    *   The `increment()` method demonstrates a critical section protected by the lock.
*   **Note**: This implementation avoids busy-waiting by using `Thread.yield()`.

### 3. Strict Alternation

*   **File**: [StrictAlternation.java](StrictAlternation.java)
*   **Description**: This algorithm allows only one process to enter the critical section based on whose turn it is.
*   **Implementation**:
    *   The `turn` variable indicates which thread's turn it is to enter the critical section.
    *   Each thread waits until it is its turn (`while (turn != id)`).
    *   After executing the critical section, the thread sets the `turn` to the other thread.
*   **Note**: Strict alternation can lead to inefficiency if one thread is consistently slower than the other.

### 4. Peterson's Algorithm (Mutual Exclusion)

*   **File**: [MutualExclusion.java](MutualExclusion.java)
*   **Description**: Peterson's algorithm is a software-based solution for mutual exclusion between two processes.
*   **Implementation**:
    *   The `flag` array indicates if a process wants to enter the critical section.
    *   The `turn` variable resolves conflicts when both processes want to enter the critical section simultaneously.
    *   A process sets its `flag` to `true`, indicates its interest, and sets `turn` to the other process. It then waits as long as the other process is also interested and it is the other process's turn.
*   **Note**: Peterson's algorithm is limited to two processes.

### 5. Semaphores

*   **File**: [Semaphores.java](Semaphores.java)
*   **Description**: Semaphores are a synchronization primitive that uses a counter to control access to shared resources.
*   **Implementation**:
    *   The `Semaphore` class maintains a count of available permits.
    *   The `acquire()` method decrements the permit count (waiting if necessary).
    *   The `release()` method increments the permit count and notifies waiting threads.
*   **Note**: Semaphores can be used to implement both mutual exclusion and synchronization.

### 6. Monitors

*   **File**: [Monitors.java](Monitors.java)
*   **Description**: Monitors provide a high-level synchronization mechanism by encapsulating shared data and the operations that access it.
*   **Implementation**:
    *   The `enter()` method acquires exclusive access to the monitor (waiting if necessary).
    *   The `exit()` method releases the monitor, allowing other threads to enter.
    *   The `performTask()` method demonstrates a critical section protected by the monitor.
*   **Note**: Java's `synchronized` keyword provides built-in monitor functionality.

### 7. Message Passing

*   **File**: [MessagePassing.java](MessagePassing.java)
*   **Description**: Processes communicate by exchanging messages.
*   **Implementation**:
    *   The `send()` method sends a message to the receiver, waiting if the buffer is full.
    *   The `receive()` method receives a message from the sender, waiting if the buffer is empty.
    *   A simple producer-consumer example is implemented using message passing.
*   **Note**: Message passing can be used for both synchronization and data transfer.

## How to Run the Code

Each file contains a `main` method that demonstrates the algorithm. To run a specific algorithm, compile and execute the corresponding Java file. For example:

```sh
javac Semaphores.java
java Semaphores
```