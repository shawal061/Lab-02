import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GroceryQueues {
    private final List<GroceryQueue> queues;
    private final int maxQueueLength;
    private final int speedUpFactor;
    private final Random random;
    private final List<Thread> threads;

    public GroceryQueues(int numberOfQueues, int maxQueueLength, int speedUpFactor) {
        this.queues = new ArrayList<>(numberOfQueues);
        for (int i = 0; i < numberOfQueues; i++) {
            queues.add(new GroceryQueue(maxQueueLength, speedUpFactor));
        }
        this.maxQueueLength = maxQueueLength;
        this.speedUpFactor = speedUpFactor;
        this.random = new Random();
        this.threads = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        GroceryQueue shortestQueue = null;
        int minLength = Integer.MAX_VALUE;

        for (GroceryQueue queue : queues) {
            int length = queue.getQueueLength();
            if (length < minLength) {
                minLength = length;
                shortestQueue = queue;
            }
        }

        if (shortestQueue != null && minLength < maxQueueLength) {
            if (!shortestQueue.addCustomer(customer)) {
                customer.setServed(false); // Customer not added because the queue is full
            }
        } else {
            new Thread(() -> {
                try {
                    Thread.sleep(10000 / speedUpFactor);
                    GroceryQueue currentShortestQueue = null;
                    int currentMinLength = Integer.MAX_VALUE;

                    for (GroceryQueue queue : queues) {
                        int length = queue.getQueueLength();
                        if (length < currentMinLength) {
                            currentMinLength = length;
                            currentShortestQueue = queue;
                        }
                    }

                    if (currentShortestQueue != null && currentMinLength < maxQueueLength) {
                        if (!currentShortestQueue.addCustomer(customer)) {
                            customer.setServed(false); // Customer not added because the queue is full
                        }
                    } else {
                        customer.setServed(false); // Customer leaves without being served
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    public void serveCustomers() {
        for (GroceryQueue queue : queues) {
            Thread thread = new Thread(queue::serveCustomers);
            thread.start();
            threads.add(thread);
        }
    }

    public void stopServing() {
        for (GroceryQueue queue : queues) {
            queue.stopServing();
        }
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
