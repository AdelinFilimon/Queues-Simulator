package strategy;

import client.Client;
import simulation.Server;

import java.util.concurrent.ArrayBlockingQueue;

public class ConcreteStrategyTime implements Strategy {

    public void addClient(ArrayBlockingQueue<Server> servers, Client client) {
        Server minTimeServer = servers.peek();
        for(Server server : servers) {
            if(server.getWaitingPeriod() < minTimeServer.getWaitingPeriod()) minTimeServer = server;
        }
        assert minTimeServer != null;
        minTimeServer.addClient(client);
    }
}
