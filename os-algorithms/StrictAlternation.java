public class StrictAlternation {
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