package edu.iis.mto.serverloadbalancer;

import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasCurrentLoadOf;
import static edu.iis.mto.serverloadbalancer.ServerBuilder.server;
import static edu.iis.mto.serverloadbalancer.ServerVmsCountMatcher.hasAVmsCountOf;
import static edu.iis.mto.serverloadbalancer.VmBuilder.vm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class ServerLoadBalancerTest {

    @Test
    public void itCompiles() {
        assertThat(true, equalTo(true));
    }

    @Test
    public void balancingServerWithNoVms_serverStaysEmpty() {
        Server theServer = a(server().withCapacity(1));
        balancing(aServerListWith(theServer), anEmptyListOfVms());

        assertThat(theServer, hasCurrentLoadOf(0.0d));
    }

    @Test
    public void balancingOneServerWithOneSlotCapacity_andOneSlotVm_fillsTheServerWithTheVm() {
        Server theServer = a(server().withCapacity(1));
        Vm theVm = a(vm().ofSize(1));
        balancing(aServerListWith(theServer), aVmsListWith(theVm));

        assertThat(theServer, hasCurrentLoadOf(100.0d));
        assertThat("server should contain the vm", theServer.contains(theVm));
    }

    @Test
    public void balancingOneServerWithTenSlotCapacity_andOneSlotVm_fillsTheServerWithTenPercent() {
        Server theServer = a(server().withCapacity(10));
        Vm theVm = a(vm().ofSize(1));
        balancing(aServerListWith(theServer), aVmsListWith(theVm));

        assertThat(theServer, hasCurrentLoadOf(10.0d));
        assertThat("server should contain the vm", theServer.contains(theVm));
    }

    @Test
    public void balancingAServerWithEnoughRoom_getsFilledWithAllVms() {
        Server theServer = a(server().withCapacity(100));
        Vm theFirstVm = a(vm().ofSize(1));
        Vm theSecondVm = a(vm().ofSize(1));
        balancing(aServerListWith(theServer), aVmsListWith(theFirstVm, theSecondVm));

        assertThat(theServer, hasAVmsCountOf(2));
        assertThat("server should contain the first vm", theServer.contains(theFirstVm));
        assertThat("server should contain the second vm", theServer.contains(theSecondVm));
    }

    @Test
    public void aVm_shouldBeBalanced_onLessLoadedServerFirst() {
        Server moreLoadedServer = a(server().withCapacity(100)
                                            .withCurrentLoad(50.0d));
        Server lessLoadedServer = a(server().withCapacity(100)
                                            .withCurrentLoad(45.0d));
        Vm theVm = a(vm().ofSize(10));
        balancing(aServerListWith(moreLoadedServer, lessLoadedServer), aVmsListWith(theVm));
        assertThat("less loaded server should contain the vm", lessLoadedServer.contains(theVm));
        assertThat("more loaded server should not contain the vm", !moreLoadedServer.contains(theVm));
    }

    @Test
    public void balanceAServerWithNotEnoughRoom_shouldNotBeFilledWithAVm() {
        Server theServer = a(server().withCapacity(10)
                                     .withCurrentLoad(90.0d));
        Vm theVm = a(vm().ofSize(2));
        balancing(aServerListWith(theServer), aVmsListWith(theVm));
        assertThat("server should not contain the vm", !theServer.contains(theVm));
    }

    private Vm[] aVmsListWith(Vm... vms) {
        return vms;
    }

    private void balancing(Server[] servers, Vm[] vms) {
        new ServerLoadBalancer().balance(servers, vms);
    }

    private Vm[] anEmptyListOfVms() {
        return new Vm[0];
    }

    private Server[] aServerListWith(Server... servers) {
        return servers;
    }

    private <T> T a(Builder<T> builder) {
        return builder.build();
    }
}
