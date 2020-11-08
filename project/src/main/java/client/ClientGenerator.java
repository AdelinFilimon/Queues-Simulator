package client;

import java.util.ArrayList;
import java.util.Random;

public class ClientGenerator {
    private final int size;

    public ClientGenerator(int size) {
        if(size < 0) throw new IllegalArgumentException("Invalid size parameter");
        this.size = size;
    }

    public Client generateClient(int id, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        if(maxArrivalTime < minArrivalTime) throw new IllegalArgumentException("Invalid arrival time parameters");
        if(maxServiceTime < minServiceTime) throw new IllegalArgumentException("Invalid service time parameters");
        Random random = new Random();
        int randomArrivalTime = random.nextInt((maxArrivalTime - minArrivalTime) + 1) + minArrivalTime;
        int randomServiceTime = random.nextInt((maxServiceTime - minServiceTime) + 1) + minServiceTime;
        return new Client(id, randomArrivalTime, randomServiceTime);
    }

    public ArrayList<Client> generateClients(int size, int minArrivalTime, int maxArrivalTime, int minServiceTime,
                                                    int maxServiceTime) {

        if(size <= 0) throw new IllegalArgumentException("Invalid size parameter");
        ArrayList<Client> clients = new ArrayList<Client>();
        for(int i = 1; i <= size; i++) {
            clients.add(generateClient(i, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime));
        }
        return clients;
    }

    public ArrayList<Client> generateClients(int minArrivalTime, int maxArrivalTime, int minServiceTime,
                                             int maxServiceTime) {
        return generateClients(size, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
    }

}
