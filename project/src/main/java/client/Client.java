package client;

import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private final int id;
    private final int arrivalTime;
    private AtomicInteger serviceTime;
    private AtomicInteger waitingTime;

    public Client(int id, int arrivalTime, int serviceTime) {
        if(id < 1) throw  new IllegalArgumentException("Illegal id");
        if(arrivalTime < 0) throw new IllegalArgumentException("Illegal tArrival");
        if(serviceTime < 0) throw new IllegalArgumentException("Illegal tService");
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = new AtomicInteger(serviceTime);
        waitingTime = new AtomicInteger(0);
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime.get();
    }

    public int getWaitingTime() {
        return waitingTime.get();
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime.set(waitingTime);
    }

    public void decrementServiceTime() {
        serviceTime.decrementAndGet();
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", id, arrivalTime, serviceTime.get());
    }
}
