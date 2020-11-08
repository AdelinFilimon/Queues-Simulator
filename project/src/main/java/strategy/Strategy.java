package strategy;

import client.Client;
import simulation.Server;

import java.util.concurrent.ArrayBlockingQueue;

public interface Strategy {
    void addClient(ArrayBlockingQueue<Server> servers, Client client);
}
