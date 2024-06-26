import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.TimeUnit;

public class GroceryQueue {
    private final BlockingQueue<Customer> queue;
    private final int speedUpFactor;
    private final AtomicBoolean running;

    public GroceryQueue(int maxLength, int speedUpFactor) {
        this.queue = new LinkedBlockingQueue<>(maxLength);
        this.speedUpFactor = speedUpFactor;
        this.running = new AtomicBoolean(true);
    }

    public boolean addCustomer(Customer customer) {
        return queue.offer(customer);
    }

    public void serveCustomers() {
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
        System.out.println("GroceryQueue thread terminating.");
    }

    public int getQueueLength() {
        return queue.size();
    }

    public void stopServing() {
        running.set(false);
    }
}
