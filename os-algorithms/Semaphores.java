public class Semaphores {

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
