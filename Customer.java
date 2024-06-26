public class Customer {
    private final long arrivalTime;
    private final long serviceTime; // Original service time without speed-up adjustment
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
