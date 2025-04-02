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
