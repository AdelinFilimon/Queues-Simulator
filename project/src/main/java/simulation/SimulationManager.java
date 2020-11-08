package simulation;

import client.Client;
import client.ClientGenerator;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimulationManager implements Runnable {
    private final int timeLimit;
    private final LinkedBlockingQueue<Client> clients;
    private int nrOfClients;
    private Scheduler scheduler;
    private FileWriter output;

    public SimulationManager(int timeLimit, int nrOfServers, String output) throws IOException {
        this.timeLimit = timeLimit;
        clients = new LinkedBlockingQueue<Client>();
        scheduler = new Scheduler(nrOfServers);
        this.output = new FileWriter(output);
    }

    public void setRandomClients(int nrOfClients, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        ClientGenerator clientGenerator = new ClientGenerator(nrOfClients);
        clients.addAll(clientGenerator.generateClients(minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime));
        this.nrOfClients = clients.size();
    }

    public void run() {
        int simulationTime = 0;
        while(simulationTime <= timeLimit) {
            boolean areQueuesEmpty = scheduler.areQueuesEmpty();
            if(clients.isEmpty() && areQueuesEmpty) break;
            for(Client client : clients) {
                if(client.getArrivalTime() == simulationTime) {
                    scheduler.dispatchClient(client);
                    clients.remove(client);
                }
            }
            try { print(simulationTime); }
            catch (IOException e) { e.printStackTrace(); }
            scheduler.tryOpenQueues();
            ++simulationTime;
            try { Thread.sleep(1000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
        scheduler.closeQueues();
        try {
            output.write("Average waiting time: " + getAverageWaitingTime());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print(int time) throws IOException {
        output.write("Time " + time + "\n");
        output.write("Waiting clients: ");
        for(Client client : clients) output.write(client.toString() + "; ");
        output.write("\n");
        ArrayBlockingQueue<Server> servers = scheduler.getServers();
        int count = 1;
        for(Server server : servers) {
            output.write("Queue " + count + ": ");
            if(server.getClients().isEmpty()) output.write("closed\n");
            else {
                LinkedBlockingQueue<Client> clients = server.getClients();
                for (Client client : clients) {
                    output.write(client + "; ");
                }
                output.write("\n");
            }
            count++;
        }
        output.write("\n");
    }

    public double getAverageWaitingTime() {
        int totalWaitingTime = 0;
        for(Server server : scheduler.getServers()) {
            totalWaitingTime += server.getTotalWaitingTime();
        }

        return (double) totalWaitingTime / nrOfClients;
    }


    public static void main(String[] args) throws IOException {
        File input;
        Scanner scanner;
        Integer[] parameters;
        input = new File(args[0]);
        scanner = new Scanner(input);
        parameters = new Integer[7];
        int i = 0;
        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] res = line.split(",");
            for(String nr : res) {
                parameters[i] = Integer.parseInt(nr);
                i++;
            }
        }

        scanner.close();
        SimulationManager simulationManager = new SimulationManager(parameters[2], parameters[1], args[1]);
        simulationManager.setRandomClients(parameters[0], parameters[3], parameters[4], parameters[5], parameters[6]);
        Thread thread = new Thread(simulationManager);
        thread.start();
    }
}
