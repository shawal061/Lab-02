import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BankQueue {

    private final BlockingQueue <Customer> queue;
    private final int tellers;

    public BankQueue(int tellers, int maxLength) {
        this.queue = new LinkedBlockingQueue<>(maxLength);
        this.tellers = tellers;
    }

    public boolean addCustomer(Customer customer) {
        return queue.offer(customer);
    }

    public void serveCustomers() {
        for (int i = 0; i < tellers; i++) {
            new Thread(()->{
                while (true) {
                    serveCustomer();
                }
            }).start();
        }
    }

    private void serveCustomer() {
        try {
            Customer customer = queue.take();
            Thread.sleep(customer.getServiceTime()*1000);
            customer.setServed(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
