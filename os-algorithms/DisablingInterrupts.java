public class DisablingInterrupts {
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