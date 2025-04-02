public class Monitors {
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
