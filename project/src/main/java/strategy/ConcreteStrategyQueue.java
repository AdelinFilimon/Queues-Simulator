package strategy;

import client.Client;
import simulation.Server;

import java.util.concurrent.ArrayBlockingQueue;

public class ConcreteStrategyQueue implements Strategy {

    public void addClient(ArrayBlockingQueue<Server> servers, Client client) {
        Server minClientsServer = servers.peek();
        for(Server server : servers) {
            if(server.getClients().size() < minClientsServer.getClients().size()) minClientsServer = server;
        }
        assert minClientsServer != null;
        minClientsServer.addClient(client);
    }
}
