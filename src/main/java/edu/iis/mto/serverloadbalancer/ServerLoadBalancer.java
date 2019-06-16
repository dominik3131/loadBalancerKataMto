package edu.iis.mto.serverloadbalancer;

import java.util.ArrayList;
import java.util.List;

public class ServerLoadBalancer {

    public void balance(Server[] servers, Vm[] vms) {
        for (Vm vm : vms) {
            addToCapableLessLoadedServer(servers, vm);
        }
    }

    private void addToCapableLessLoadedServer(Server[] servers, Vm vm) {
        List<Server> capableServers = FindServersWithEnoughCapacity(servers, vm);
        Server lessLoadedServer = extractLessLoadedServer(capableServers);
        if (lessLoadedServer != null) {
            lessLoadedServer.addVm(vm);
        }
    }

    private List<Server> FindServersWithEnoughCapacity(Server[] servers, Vm vm) {
        List<Server> capableServers = new ArrayList<>();
        for (Server server : servers) {
            if (server.canFit(vm)) {
                capableServers.add(server);
            }
        }
        return capableServers;
    }

    private Server extractLessLoadedServer(List<Server> capableServers) {
        Server lessLoadedServer = null;
        for (Server server : capableServers) {
            if (lessLoadedServer == null || lessLoadedServer.getCurrentLoadPercentage() > server.getCurrentLoadPercentage()) {
                lessLoadedServer = server;
            }
        }
        return lessLoadedServer;
    }

}
