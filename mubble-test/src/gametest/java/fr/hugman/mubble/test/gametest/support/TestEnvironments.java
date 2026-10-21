package fr.hugman.mubble.test.gametest.support;

/**
 * The time of day a test runs at, for the tests whose subject sleeps or only comes out by day.
 * <p>
 * Game tests start at night. Setting the clock from inside a test is not the way to change that: the
 * clock belongs to the server, so a test that sets it sets it for every other test running beside it
 * in the same batch, and whichever one writes last wins. A test environment is applied to a batch of
 * its own and put back afterwards, so the time a test asks for is the time it gets.
 */
public final class TestEnvironments {
    /** Sun overhead. */
    public static final String MIDDAY = "mubble-gametest:midday";
    /** Dark, which is where a game test would start anyway: said out loud so no neighbour can move it. */
    public static final String MIDNIGHT = "mubble-gametest:midnight";

    private TestEnvironments() {
    }
}
