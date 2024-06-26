import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BankQueue {
    private final BlockingQueue<Customer> queue;
    private final int tellers;
    private final int speedUpFactor;
    private final List<Thread> threads;
    private final AtomicBoolean running;

    public BankQueue(int tellers, int maxLength, int speedUpFactor) {
        this.queue = new LinkedBlockingQueue<>(maxLength);
        this.tellers = tellers;
        this.speedUpFactor = speedUpFactor;
        this.threads = new ArrayList<>();
        this.running = new AtomicBoolean(true);
    }

    public boolean addCustomer(Customer customer) {
        return queue.offer(customer);
    }

    public void serveCustomers() {
        for (int i = 0; i < tellers; i++) {
            Thread thread = new Thread(() -> {
                while (running.get()) {
                    try {
                        Customer customer = queue.poll(1, TimeUnit.SECONDS);
                        if (customer != null) {
                            long adjustedServiceTime = customer.getServiceTime() * 1000 / speedUpFactor;
                            Thread.sleep(adjustedServiceTime);
                            customer.setServed(true);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("BankQueue thread terminating.");
            });
            thread.start();
            threads.add(thread);
        }
    }

    public void stopServing() {
        running.set(false);
        for (Thread thread : threads) {
            thread.interrupt();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
