package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.references.GoombaVariantIds;
import fr.hugman.mubble.super_mario.core.registries.SuperMarioRegistries;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.Goomba;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

/**
 * The goomba, and the two states it carries beyond a vanilla mob: being surprised, which doubles as
 * being stunned, and its variant, which comes from a data pack.
 */
public class GoombaGameTest {
    private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    @GameTest
    public void agoombaStartsCalm(GameTestHelper helper) {
        var goomba = goomba(helper);

        helper.assertFalse(goomba.isSurprised(), "a fresh goomba is already surprised");
        helper.assertFalse(goomba.isStunned(), "a fresh goomba is already stunned");
        helper.assertValueEqual(goomba.getSurpriseProgress(), 0, "the surprise progress of a fresh goomba");

        helper.succeed();
    }

    /** Being surprised is what holds the goomba still, so the two have to stay in step. */
    @GameTest
    public void beingSurprisedIsBeingStunned(GameTestHelper helper) {
        var goomba = goomba(helper);

        goomba.setSurprised(true);
        helper.assertTrue(goomba.isStunned(), "a surprised goomba should be stunned");

        goomba.setSurprised(false);
        helper.assertFalse(goomba.isStunned(), "a goomba over its surprise should move again");

        helper.succeed();
    }

    @GameTest
    public void thesurpriseWearsOff(GameTestHelper helper) {
        var goomba = goomba(helper);
        goomba.setSurprised(true);

        for (int i = 0; i <= Goomba.SURPRISE_LENGTH + 1; i++) {
            goomba.tick();
        }

        helper.assertFalse(goomba.isSurprised(), "the surprise never wore off");
        helper.assertValueEqual(goomba.getSurpriseProgress(), 0, "the progress should be wound back once the surprise is over");

        helper.succeed();
    }

    @GameTest
    public void thesurpriseProgressesOneTickAtATime(GameTestHelper helper) {
        var goomba = goomba(helper);
        goomba.setSurprised(true);

        goomba.tick();
        helper.assertValueEqual(goomba.getSurpriseProgress(), 1, "the surprise progress after one tick");

        goomba.tick();
        helper.assertValueEqual(goomba.getSurpriseProgress(), 2, "the surprise progress after two ticks");

        helper.succeed();
    }

    @GameTest
    public void acalmGoombaDoesNotProgress(GameTestHelper helper) {
        var goomba = goomba(helper);

        goomba.tick();
        goomba.tick();

        helper.assertValueEqual(goomba.getSurpriseProgress(), 0, "a calm goomba should not be counting anything down");
        helper.succeed();
    }

    @GameTest
    public void agoombaHasAVariant(GameTestHelper helper) {
        var goomba = goomba(helper);

        helper.assertTrue(goomba.getVariant().is(GoombaVariantIds.NORMAL), "a goomba should default to the normal variant");
        helper.succeed();
    }

    @GameTest
    public void thevariantSurvivesSaveAndLoad(GameTestHelper helper) {
        var goomba = goomba(helper);
        var mini = helper.getLevel().registryAccess()
                .lookupOrThrow(SuperMarioRegistries.GOOMBA_VARIANT).getOrThrow(GoombaVariantIds.MINI);
        goomba.setVariant(mini);

        var registries = helper.getLevel().registryAccess();
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        goomba.saveWithoutId(output);

        var reloaded = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND);
        reloaded.load(TagValueInput.create(ProblemReporter.DISCARDING, registries, output.buildResult()));

        helper.assertTrue(reloaded.getVariant().is(GoombaVariantIds.MINI), "the variant was lost through the save file");
        helper.succeed();
    }

    /** A variant carries attribute modifiers, so picking one has to actually change the goomba. */
    @GameTest
    public void theminiVariantIsSmallerThanTheNormalOne(GameTestHelper helper) {
        var normal = goomba(helper);
        var mini = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND);
        mini.setVariant(helper.getLevel().registryAccess()
                .lookupOrThrow(SuperMarioRegistries.GOOMBA_VARIANT).getOrThrow(GoombaVariantIds.MINI));

        helper.assertTrue(mini.getBbWidth() < normal.getBbWidth() || mini.getMaxHealth() != normal.getMaxHealth(),
                "the mini variant should differ from the normal one somewhere");

        helper.succeed();
    }

    private static Goomba goomba(GameTestHelper helper) {
        Arena.buildFloor(helper);
        return helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND);
    }
}
