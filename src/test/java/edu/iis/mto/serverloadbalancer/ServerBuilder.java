package edu.iis.mto.serverloadbalancer;

public class ServerBuilder implements Builder<Server> {

    private int capacity;
    private double initialLoad;

    public ServerBuilder withCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    @Override
    public Server build() {
        Server server = new Server(capacity);
        if (initialLoad > 0) {
            int vmInitialSize = (int) (initialLoad / capacity * 100.0d);
            Vm vm = new Vm(vmInitialSize);
            server.addVm(vm);
        }

        return server;
    }

    public static ServerBuilder server() {
        return new ServerBuilder();
    }

    public ServerBuilder withCurrentLoad(double initialLoad) {
        this.initialLoad = initialLoad;
        return this;
    }
}
