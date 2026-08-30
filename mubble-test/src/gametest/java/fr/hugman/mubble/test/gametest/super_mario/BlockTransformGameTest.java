package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.attribute.SuperMarioEnvironmentAttributes;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.world.attribute.BlockTransform;
import fr.hugman.mubble.world.entity.projectile.Ball;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * What the balls do to the blocks they hit. The transforms are not written in code where they are
 * used: the biome modifications put them on the biome as environment attributes, the tags say which
 * blocks they apply to, and the projectiles only read the result. This walks the whole chain.
 */
public class BlockTransformGameTest {
    private static final int FLOOR = Arena.FLOOR_Y;
    private static final BlockPos TARGET = new BlockPos(4, FLOOR + 1, 3);

    @GameTest(maxTicks = 120)
    public void afireballMeltsIceIntoWater(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TARGET, Blocks.ICE);

        shootAt(helper, SuperMarioEntityTypes.FIREBALL);

        helper.succeedWhen(() -> helper.assertBlockPresent(Blocks.WATER, TARGET));
    }

    @GameTest(maxTicks = 120)
    public void afireballMeltsPackedIceIntoIce(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TARGET, Blocks.PACKED_ICE);

        shootAt(helper, SuperMarioEntityTypes.FIREBALL);

        helper.succeedWhen(() -> helper.assertBlockPresent(Blocks.ICE, TARGET));
    }

    @GameTest(maxTicks = 120)
    public void anIceballFreezesIceIntoPackedIce(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TARGET, Blocks.ICE);

        shootAt(helper, SuperMarioEntityTypes.ICEBALL);

        helper.succeedWhen(() -> helper.assertBlockPresent(Blocks.PACKED_ICE, TARGET));
    }

    /** The transforms are meant for the meltable blocks alone, not for whatever the ball runs into. */
    @GameTest(maxTicks = 120)
    public void afireballLeavesStoneAlone(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TARGET, Blocks.STONE);

        var fireball = shootAt(helper, SuperMarioEntityTypes.FIREBALL);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(fireball.isRemoved(), "the fireball never hit anything"))
                .thenExecute(() -> helper.assertBlockPresent(Blocks.STONE, TARGET))
                .thenSucceed();
    }

    @GameTest(maxTicks = 120)
    public void anIceballLeavesPackedIceAlone(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TARGET, Blocks.PACKED_ICE);

        var iceball = shootAt(helper, SuperMarioEntityTypes.ICEBALL);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(iceball.isRemoved(), "the iceball never hit anything"))
                .thenExecute(() -> helper.assertBlockPresent(Blocks.PACKED_ICE, TARGET))
                .thenSucceed();
    }

    /**
     * The transforms live on the biome, and the first matching one wins, so their order is part of
     * their meaning: ice is both meltable to water and freezable, depending on which list is read.
     */
    @GameTest
    public void theBiomeCarriesTheTransforms(GameTestHelper helper) {
        var attributes = helper.getLevel().environmentAttributes();
        var absolute = helper.absolutePos(TARGET);

        var melts = attributes.getValue(SuperMarioEnvironmentAttributes.FIREBALL_MELTS, absolute);
        var freezes = attributes.getValue(SuperMarioEnvironmentAttributes.ICEBALL_FREEZES, absolute);

        helper.assertFalse(melts.isEmpty(), "no melting transform reached the biome, nothing would ever melt");
        helper.assertFalse(freezes.isEmpty(), "no freezing transform reached the biome, nothing would ever freeze");

        var ice = BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.ICE);
        helper.assertTrue(BlockTransform.testList(melts, ice) != null, "ice should be meltable");
        helper.assertTrue(BlockTransform.testList(freezes, ice) != null, "ice should be freezable");
        helper.assertTrue(BlockTransform.testList(melts, BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.STONE)) == null,
                "stone should match no transform at all");

        helper.succeed();
    }

    /** Fires the ball horizontally at {@code TARGET}, gravity off so only the impact matters. */
    private static <T extends Ball> T shootAt(GameTestHelper helper, EntityType<T> type) {
        var spawn = new Vec3(TARGET.getX() + 0.5D, TARGET.getY() + 0.5D, TARGET.getZ() - 2.0D);

        T ball = helper.spawn(type, spawn);
        ball.setNoGravity(true);
        ball.setDeltaMovement(0.0D, 0.0D, 0.4D);
        return ball;
    }
}
