package edu.iis.mto.serverloadbalancer;

import java.util.ArrayList;
import java.util.List;

public class Server {

    public static final double MAXIMUM_LOAD = 100.0d;
    public double currentLoadPercentage;
    public int capacity;
    private List<Vm> VmsList = new ArrayList<>();

    public boolean contains(Vm theVm) {
        return VmsList.contains(theVm);
    }

    public Server(int capacity) {
        super();
        this.capacity = capacity;
    }

    public void addVm(Vm vm) {
        currentLoadPercentage = (double) vm.size / (double) capacity * MAXIMUM_LOAD;
        VmsList.add(vm);
    }

    public int countVms() {
        return VmsList.size();
    }

}
