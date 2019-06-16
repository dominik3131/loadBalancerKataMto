package edu.iis.mto.serverloadbalancer;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class CurrentLoadPercentageMatcher extends TypeSafeMatcher<Server> {

    private double expectedLoadPercentage;

    public CurrentLoadPercentageMatcher(double expectedLoadPercentage) {
        this.expectedLoadPercentage = expectedLoadPercentage;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a server with load percentage of ")
                   .appendValue(expectedLoadPercentage);
    }

    @Override
    protected boolean matchesSafely(Server server) {
        return doublesAreEqual(expectedLoadPercentage, server.currentLoadPercentage);
    }

    private boolean doublesAreEqual(double expectedLoadPercentage2, double currentLoadPercentage) {
        return expectedLoadPercentage2 == currentLoadPercentage || Math.abs(expectedLoadPercentage2 - currentLoadPercentage) < 0.01d;
    }

    public static CurrentLoadPercentageMatcher hasCurrentLoadOf(double expectedLoadPercentage) {
        return new CurrentLoadPercentageMatcher(expectedLoadPercentage);
    }

}
