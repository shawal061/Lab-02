import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QueueSimulator {
    private final List<Customer> customers;
    private final Random random;
    private final BankQueue bankQueue;
    private final GroceryQueues groceryQueues;
    private final int simulationTimeInSeconds;
    private final int speedUpFactor;

    public QueueSimulator(int bankTellers, int bankQueueMaxLength, int groceryCashiers, int groceryQueueMaxLength, int simulationTimeInMinutes, int speedUpFactor) {
        this.customers = new ArrayList<>();
        this.random = new Random();
        this.bankQueue = new BankQueue(bankTellers, bankQueueMaxLength, speedUpFactor);
        this.groceryQueues = new GroceryQueues(groceryCashiers, groceryQueueMaxLength, speedUpFactor);
        this.simulationTimeInSeconds = simulationTimeInMinutes * 60;
        this.speedUpFactor = speedUpFactor;
    }

    public void runSimulation() {
        bankQueue.serveCustomers();
        groceryQueues.serveCustomers();

        int customerArrivalInterval = Math.max(1, 41 / speedUpFactor); // Ensure positive interval

        for (int time = 0; time < simulationTimeInSeconds; time++) {
            if (random.nextInt(customerArrivalInterval) == 0) { // New customer arrives between 20 to 60 seconds
                long arrivalTime = time;
                long serviceTime = (random.nextInt(241) + 60); // Service time between 60 to 300 seconds
                Customer customer = new Customer(arrivalTime, serviceTime);
                customers.add(customer);

                if (!bankQueue.addCustomer(customer)) {
                    customer.setServed(false);
                }
                groceryQueues.addCustomer(customer);
            }
            try {
                Thread.sleep(1000 / speedUpFactor); // Simulate clock tick
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        bankQueue.stopServing();
        groceryQueues.stopServing();
    }

    public void printStatistics() {
        long totalCustomers = customers.size();
        long servedCustomers = customers.stream().filter(Customer::isServed).count();
        long leftCustomers = totalCustomers - servedCustomers;
        long totalServiceTime = customers.stream().filter(Customer::isServed).mapToLong(Customer::getServiceTime).sum();
        double averageServiceTime = servedCustomers == 0 ? 0 : (double) totalServiceTime / servedCustomers;

        System.out.println("Total customers: " + totalCustomers);
        System.out.println("Total customers served: " + servedCustomers);
        System.out.println("Total customers left without being served: " + leftCustomers);
        System.out.println("Average service time: " + averageServiceTime);
    }
}
