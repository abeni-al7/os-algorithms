#include <iostream>

void disable_interrupts() {
    asm volatile ("cli" ::: "memory");
}

void enable_interrupts() {
    asm volatile ("sti" ::: "memory");
}

void performCriticalOperation() {
    std::cout << "Inside critical section." << std::endl;
    // Simulate some work in the critical section.
    for (volatile int i = 0; i < 1000000; ++i) {
        // ... performing critical work ...
    }
    std::cout << "Exiting critical section." << std::endl;
}

int main() {
    std::cout << "Starting critical operation with interrupts disabled." << std::endl;
   
    disable_interrupts();
    
    performCriticalOperation();
    
    enable_interrupts();
    
    std::cout << "Critical operation finished, interrupts enabled again." << std::endl;
    return 0;
}
