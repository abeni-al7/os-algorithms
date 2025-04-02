import java.util.concurrent.atomic.AtomicBoolean;

public class LockVariables {
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
