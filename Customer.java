// Represents a customer with their arrival time, service time, and whether they were served or not.

public class Customer {

    private final long arrivalTime;
    private final long serviceTime;
    private boolean served;

    public Customer(long arrivalTime, long serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.served = false;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public long getServiceTime() {
        return serviceTime;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }

}
