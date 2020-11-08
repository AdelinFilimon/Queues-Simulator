package simulation;

import client.Client;
import strategy.ConcreteStrategyQueue;
import strategy.ConcreteStrategyTime;
import strategy.SelectionPolicy;
import strategy.Strategy;
import java.util.concurrent.ArrayBlockingQueue;

public class Scheduler {
    private ArrayBlockingQueue<Server> servers;
    private Strategy strategy;

    public Scheduler(int maxNoOfServers) {
        servers = new ArrayBlockingQueue<Server>(maxNoOfServers);
        for(int i = 0; i < maxNoOfServers; i++) {
            Server server = new Server();
            servers.add(server);
        }
        strategy = new ConcreteStrategyTime();
    }

    public void changeStrategy(SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE) strategy = new ConcreteStrategyQueue();
        else if(policy == SelectionPolicy.SHORTEST_TIME) strategy = new ConcreteStrategyTime();
    }

    public void dispatchClient(Client client) {
        strategy.addClient(servers, client);
    }

    public ArrayBlockingQueue<Server> getServers() {
        return servers;
    }

    public boolean areQueuesEmpty() {
        for(Server server : servers) {
            if(!server.getClients().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void tryOpenQueues() {
        for(Server server : servers) {
            if(!server.getFlag() && !server.getClients().isEmpty()){
                server.setFlag(true);
                Thread thread = new Thread(server);
                thread.start();
            }
        }
    }

    public void closeQueues() {
        for(Server server : servers) {
            server.setFlag(false);
        }
    }
}
