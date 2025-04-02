public class MessagePassing {
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
