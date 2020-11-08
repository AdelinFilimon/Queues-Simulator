package simulation;

import client.Client;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private LinkedBlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    private AtomicBoolean flag;
    private AtomicInteger totalWaitingTime;

    public Server() {
        clients = new LinkedBlockingQueue<Client>();
        waitingPeriod = new AtomicInteger(0);
        flag = new AtomicBoolean(false);
        totalWaitingTime = new AtomicInteger(0);
    }


    public void addClient(Client client) {
        clients.add(client);
        totalWaitingTime.set(waitingPeriod.get() + totalWaitingTime.get() + client.getServiceTime());
        client.setWaitingTime(waitingPeriod.get() + client.getServiceTime());
        waitingPeriod.set(waitingPeriod.get() + client.getServiceTime());
    }

    public void setFlag(boolean flag) {
        this.flag.set(flag);
    }

    public boolean getFlag() {
        return flag.get();
    }

    public void run() {
        while(flag.get()) {
            if(clients.isEmpty()) break;
            Client client = clients.peek();
            if (client.getServiceTime() == 1) clients.poll();
            client.decrementServiceTime();
            waitingPeriod.decrementAndGet();
            try {
                Thread.sleep(1005);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        flag.set(false);
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public LinkedBlockingQueue<Client> getClients() {
        return clients;
    }

    public int getTotalWaitingTime() {

        for(Client client : clients) {
            totalWaitingTime.set(totalWaitingTime.get() - client.getWaitingTime());
        }

        return totalWaitingTime.get();
    }
}

