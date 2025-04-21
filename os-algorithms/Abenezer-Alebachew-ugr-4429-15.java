import java.util.concurrent.atomic.AtomicBoolean;

public class Abenezer-Alebachew-ugr-4429-15 {
    
}

private class DisablingInterrupts {
    private volatile boolean interruptsEnabled = true;

    public void enterCriticalSection() {
        interruptsEnabled = false;
    }

    public void exitCriticalSection() {
        interruptsEnabled = true;
    }

    public boolean areInterruptsEnabled() {
        return interruptsEnabled;
    }

    public static void main(String[] args) {
        DisablingInterrupts di = new DisablingInterrupts();

        System.out.println("Interrupts enabled? " + di.areInterruptsEnabled());

        di.enterCriticalSection();
        try {
            System.out.println("Inside critical section. Interrupts enabled? " + di.areInterruptsEnabled());
            // Simple simulation task
            int a = 2, b = 3;
            int result = a * b;
            System.out.println("Result of " + a + " * " + b + " is: " + result);
        } finally {
            di.exitCriticalSection();
        }
        
        System.out.println("Interrupts enabled? " + di.areInterruptsEnabled());
    }
}

private class LockVariables {
    private final AtomicBoolean lock = new AtomicBoolean(false);

    private int counter = 0; // shared resource

    public void lock() {
        while (!lock.compareAndSet(false, true)) {
            // Yield to allow other threads to run.
            Thread.yield();
        }
    }

    public void unlock() {
        lock.set(false);
    }

    // simulated critical region task
    public void increment() {
        lock();
        try {
            counter++;
        } finally {
            unlock();
        }
    }

    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) {
        LockVariables lv = new LockVariables();

        //two threads that increment the counter concurrently.
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                lv.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                lv.increment();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Expected output: 20000
        System.out.println("Final counter value: " + lv.getCounter());
    }
}


private class StrictAlternation {
    private volatile int turn = 0;
    private int counter = 0; // shared resource

    // represent a thread that executes a critical section.
    private class Worker implements Runnable {
        private final int id; // should be 0 or 1

        public Worker(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                // wait until it's this thread's turn.
                while (turn != id) {
                    Thread.yield();
                }

                counter++;
                System.out.println("Thread " + id + " in critical section, counter: " + counter);
                
                // Set turn to the other thread.
                turn = 1 - id;

                // non-critical section work.
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        StrictAlternation sa = new StrictAlternation();
        Thread t0 = new Thread(sa.new Worker(0));
        Thread t1 = new Thread(sa.new Worker(1));

        t0.start();
        t1.start();

        try {
            t0.join();
            t1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final counter value: " + sa.counter);
    }
}

private class Semaphores {

    // A simple counting semaphore
    public static class Semaphore {
        private int permits;

        public Semaphore(int permits) {
            if (permits < 0) {
                throw new IllegalArgumentException("Permit count cannot be negative.");
            }
            this.permits = permits;
        }

        public synchronized void acquire() throws InterruptedException {
            while (permits == 0) {
                wait();
            }
            permits--;
        }

        public synchronized void release() {
            permits++;
            notifyAll();
        }
    }

    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " waiting for a permit.");
                    semaphore.acquire();
                    System.out.println("Thread " + threadId + " acquired a permit.");
                    
                    // critical section work.
                    Thread.sleep(1000);
                    
                    System.out.println("Thread " + threadId + " releasing a permit.");
                    semaphore.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread " + threadId + " was interrupted.");
                }
            }).start();
        }
    }
}

private class Monitors {
    private boolean inUse = false;

    public synchronized void enter() throws InterruptedException {
        while (inUse) {
            wait();
        }
        inUse = true;
    }

    public synchronized void exit() {
        inUse = false;

        notifyAll();
    }

    public void performTask(String threadName) {
        try {
            enter();
            System.out.println(threadName + " is entering the critical section.");
            Thread.sleep(1000);
            System.out.println(threadName + " is leaving the critical section.");
            exit();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(threadName + " was interrupted.");
        }
    }

    public static void main(String[] args) {
        final Monitors monitor = new Monitors();

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            for (int i = 0; i < 3; i++) {
                monitor.performTask(threadName);
            }
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");

        t1.start();
        t2.start();
        t3.start();
    }
}

private class MessagePassing {
    private String message;
    private boolean empty = true;

    public synchronized void send(String msg) throws InterruptedException {
        while (!empty) { 
            wait();
        }
        message = msg;
        empty = false;
        notifyAll();
    }

    public synchronized String receive() throws InterruptedException {
        while (empty) {
            wait();
        }
        String msg = message;
        empty = true;
        notifyAll();
        return msg;
    }

    private class Producer implements Runnable {
        @Override
        public void run() {
            String[] messages = {
                "Hello",
                "Message passing",
                "via synchronization",
                "in Java",
                "DONE" // message to signal completion.
            };

            try {
                for (String msg : messages) {
                    System.out.println("Producer sending: " + msg);
                    send(msg);
                    // delay between messages.
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted.");
            }
        }
    }

    private class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                String msg;
                while (!(msg = receive()).equals("DONE")) {
                    System.out.println("Consumer received: " + msg);
                    Thread.sleep(500);
                }
                System.out.println("Consumer received termination signal: " + msg);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted.");
            }
        }
    }

    public static void main(String[] args) {
        MessagePassing mp = new MessagePassing();

        Thread producerThread = new Thread(mp.new Producer());
        Thread consumerThread = new Thread(mp.new Consumer());

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Message passing simulation completed.");
    }
}

public class MutualExclusion {
    private volatile boolean[] flag = new boolean[2];
    private volatile int turn;

    private int counter = 0; // shared resource

    private class Worker implements Runnable {
        private final int id;

        public Worker(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                flag[id] = true;
                turn = 1 - id;
                while (flag[1 - id] && turn == 1 - id) {
                    Thread.yield();
                }

                counter++;
                System.out.println("Thread " + id + " in critical section, counter: " + counter);

                // Exit section
                flag[id] = false;

                // Non-critical section
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        MutualExclusion me = new MutualExclusion();
        
        Thread t0 = new Thread(me.new Worker(0));
        Thread t1 = new Thread(me.new Worker(1));

        t0.start();
        t1.start();

        try {
            t0.join();
            t1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final counter value: " + me.counter);
    }
}

