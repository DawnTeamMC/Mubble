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
    public void setTrueFreezesTheTarget(GameTestHelper helper) {
        var pig = target(helper);

        run(helper, operator(helper), "freeze set " + pig.getUUID() + " true");

        helper.assertTrue(Freezing.isFrozen(pig), "the command left the pig unfrozen");
        helper.succeed();
    }

    @GameTest
    public void setFalseThawsTheTarget(GameTestHelper helper) {
        var pig = target(helper);
        var operator = operator(helper);
        run(helper, operator, "freeze set " + pig.getUUID() + " true");

        run(helper, operator, "freeze set " + pig.getUUID() + " false");

        helper.assertFalse(Freezing.isFrozen(pig), "the command left the pig in the ice");
        helper.succeed();
    }

    @GameTest
    public void omittingTheValueTogglesIt(GameTestHelper helper) {
        var pig = target(helper);
        var operator = operator(helper);

        run(helper, operator, "freeze set " + pig.getUUID());
        helper.assertTrue(Freezing.isFrozen(pig), "the first toggle froze nothing");

        run(helper, operator, "freeze set " + pig.getUUID());
        helper.assertFalse(Freezing.isFrozen(pig), "the second toggle thawed nothing");

        helper.succeed();
    }

    @GameTest
    public void settingWhatIsAlreadySetFails(GameTestHelper helper) {
        var pig = target(helper);
        var operator = operator(helper);

        helper.assertFalse(succeeds(helper, operator, "freeze set " + pig.getUUID() + " false"),
                "thawing a pig that is not frozen should fail");

        run(helper, operator, "freeze set " + pig.getUUID() + " true");
        helper.assertFalse(succeeds(helper, operator, "freeze set " + pig.getUUID() + " true"),
                "freezing a pig that is already frozen should fail");

        helper.assertTrue(Freezing.isFrozen(pig), "and the freeze should be left untouched");
        helper.succeed();
    }

    @GameTest
    public void aCreativePlayerCannotBeFrozen(GameTestHelper helper) {
        // the framework hands out creative players and nothing else, which is exactly what is needed here
        var player = TestPlayers.inLevel(helper);

        helper.assertFalse(succeeds(helper, player, "freeze set @s true"),
                "a creative player was put in a block of ice");
        helper.assertFalse(Freezing.isFrozen(player), "and ended up frozen anyway");

        helper.succeed();
    }

    @GameTest
    public void queryAnswersWithTheUsualOneOrZero(GameTestHelper helper) {
        var pig = target(helper);
        var operator = operator(helper);

        helper.assertTrue(perform(helper, operator, "freeze query " + pig.getUUID()).result == 0,
                "a query on an unfrozen entity should answer 0, so that `execute if` reads it as a no");

        run(helper, operator, "freeze set " + pig.getUUID() + " true");
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
