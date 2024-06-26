public class Main {
    public static void main(String[] args) {
        int simulationTimeInMinutes = 120; // 2 hours
        int bankTellers = 3;
        int bankQueueMaxLength = 5;
        int groceryCashiers = 3;
        int groceryQueueMaxLength = 2;
        int speedUpFactor = 480; // Speed-up factor to complete simulation in 15 seconds

        QueueSimulator simulator = new QueueSimulator(bankTellers, bankQueueMaxLength, groceryCashiers, groceryQueueMaxLength, simulationTimeInMinutes, speedUpFactor);
        simulator.runSimulation();
        simulator.printStatistics();
    }
}
