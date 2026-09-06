package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;

import static fr.hugman.mubble.test.gametest.support.TestCommands.perform;
import static fr.hugman.mubble.test.gametest.support.TestCommands.run;
import static fr.hugman.mubble.test.gametest.support.TestCommands.succeeds;

/**
 * {@code /freeze}, the way an entity is put in a block of ice by hand.
 * <p>
 * Every target is named by its UUID rather than picked with a type selector: game tests share one
 * level, and {@code @e[type=pig]} would just as happily reach into whatever another test is running.
 */
public class FreezeCommandGameTest {
    private static final BlockPos TARGET = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    @GameTest
    public void aTimeFreezesTheTarget(GameTestHelper helper) {
        var pig = target(helper);

        run(helper, operator(helper), "freeze set " + pig.getUUID() + " 100");

        helper.assertTrue(Freezing.isFrozen(pig), "the command left the pig unfrozen");
        helper.succeed();
    }

    @GameTest
    public void aTimeOfZeroThawsTheTarget(GameTestHelper helper) {
        var pig = target(helper);
        var operator = operator(helper);
        run(helper, operator, "freeze set " + pig.getUUID() + " 100");

        run(helper, operator, "freeze set " + pig.getUUID() + " 0");

        helper.assertFalse(Freezing.isFrozen(pig), "the command left the pig in the ice");
        helper.succeed();
    }

    @GameTest
    public void omittingTheValueFails(GameTestHelper helper) {
        var pig = target(helper);
        var operator = operator(helper);

        helper.assertFalse(succeeds(helper, operator, "freeze set " + pig.getUUID()),
                "the time is mandatory: leaving it off must not quietly flip the target");
        helper.assertFalse(Freezing.isFrozen(pig), "and the pig should have been left alone");

        helper.succeed();
    }

    @GameTest
    public void aDurationIsHonoured(GameTestHelper helper) {
        var pig = target(helper);

        run(helper, operator(helper), "freeze set " + pig.getUUID() + " 7");

        helper.assertTrue(Freezing.getRemainingTicks(pig) == 7,
                "the freeze should last exactly the number of ticks asked for");
        helper.succeed();
    }

    @GameTest
    public void anInfiniteFreezeNeverRunsOut(GameTestHelper helper) {
        var pig = target(helper);

        run(helper, operator(helper), "freeze set " + pig.getUUID() + " infinite");

        var state = Freezing.getState(pig);
        helper.assertTrue(state != null && state.isEndless(), "the freeze should have been endless");
        // an endless freeze outlasts anything a countdown could hold
        helper.assertFalse(state.hasExpired(pig.level().getGameTime() + 1_000_000L),
                "an endless freeze ran out anyway");
        helper.succeed();
    }

    @GameTest
    public void aNewTimeReplacesTheOldOne(GameTestHelper helper) {
        var pig = target(helper);
        var operator = operator(helper);

        run(helper, operator, "freeze set " + pig.getUUID() + " 200");
        run(helper, operator, "freeze set " + pig.getUUID() + " 20");

        helper.assertTrue(Freezing.isFrozen(pig), "re-setting the time let the pig out");
        helper.assertTrue(Freezing.getRemainingTicks(pig) == 20,
                "the second time should have replaced the first, not been turned down");
        helper.succeed();
    }

    @GameTest
    public void thawingWhatIsNotFrozenFails(GameTestHelper helper) {
        var pig = target(helper);

        helper.assertFalse(succeeds(helper, operator(helper), "freeze set " + pig.getUUID() + " 0"),
                "thawing a pig that is not frozen should fail");
        helper.succeed();
    }

    @GameTest
    public void aCreativePlayerCanBeFrozenByHand(GameTestHelper helper) {
        // the framework hands out creative players and nothing else, which is exactly what is needed here
        var player = TestPlayers.inLevel(helper);

        // an ice ball leaves a creative player alone, but the command is an operator's tool and does not
        helper.assertTrue(succeeds(helper, player, "freeze set @s 100"),
                "the command turned a creative player down");
        helper.assertTrue(Freezing.isFrozen(player), "and left them out of the ice");

        // and the freeze has to survive the tick that lets genuinely unfreezable entities out
        Freezing.tick(player);
        helper.assertTrue(Freezing.isFrozen(player), "the next tick thawed them again");

        helper.succeed();
    }

    @GameTest
    public void queryAnswersWithTheUsualOneOrZero(GameTestHelper helper) {
        var pig = target(helper);
        var operator = operator(helper);

        helper.assertTrue(perform(helper, operator, "freeze query " + pig.getUUID()).result == 0,
                "a query on an unfrozen entity should answer 0, so that `execute if` reads it as a no");

        run(helper, operator, "freeze set " + pig.getUUID() + " 100");
        helper.assertTrue(perform(helper, operator, "freeze query " + pig.getUUID()).result == 1,
                "a query on a frozen entity should answer 1");

        helper.succeed();
    }

    /** Something freezable standing in the arena, for the command to be pointed at. */
    private static Pig target(GameTestHelper helper) {
        Arena.buildFloor(helper);
        return helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
    }

    /** Whoever runs the command. Only its position and its level matter. */
    private static ServerPlayer operator(GameTestHelper helper) {
        return TestPlayers.inLevel(helper);
    }
}
