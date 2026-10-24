package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBulletConfig;
import fr.hugman.mubble.splatoon.world.item.weapon.AutomaticShooterConfig;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletDamageParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletMoveParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.SplashSpawnParam;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The weapons of the Splatoon module behave like in Splatoon 3, from the values of its parameter tables.
 */
public class SplatoonWeaponTest {
    private static final AutomaticShooterConfig SPLATTERSHOT = new AutomaticShooterConfig(ShooterInkBulletConfig.SPLATTERSHOT, 6, 4.86F, 11.66F, 0.072F, 2.0F);

    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    /**
     * Flies a bullet shot horizontally from the given height until it reaches the ground.
     *
     * @return where it landed, and after how many frames
     */
    private static double[] fly(BulletMoveParam move, double height) {
        var position = new Vec3(0, height, 0);
        var velocity = new Vec3(move.spawnSpeed(), 0, 0);
        int frame = 0;
        while (position.y > 0 && frame < 1000) {
            velocity = move.nextVelocity(velocity, frame);
            position = position.add(velocity);
            frame++;
        }
        return new double[]{position.x, frame};
    }

    @Test
    @DisplayName("a bullet keeps its spawn speed for its whole straight state")
    void theStraightStateGoesStraight() {
        var move = ShooterInkBulletConfig.SPLATTERSHOT.move();
        var velocity = new Vec3(move.spawnSpeed(), 0, 0);
        for (int frame = 0; frame < move.goStraightToBrakeStateFrame(); frame++) {
            velocity = move.nextVelocity(velocity, frame);
            assertEquals(move.spawnSpeed(), velocity.x, 1.0E-6, "the bullet slowed down on frame " + frame);
            assertEquals(0.0, velocity.y, 1.0E-6, "the bullet fell on frame " + frame);
        }

        // then its speed is capped, and the brake state starts biting right away
        velocity = move.nextVelocity(velocity, move.goStraightToBrakeStateFrame());
        assertEquals(move.goStraightStateEndMaxSpeed() * (1.0 - move.brakeAirResist()), velocity.x, 1.0E-6);
        assertEquals(-move.brakeGravity(), velocity.y, 1.0E-6);
    }

    @Test
    @DisplayName("a Splattershot shot reaches about 12 blocks")
    void theSplattershotReachesAsFarAsInSplatoon() {
        var landing = fly(ShooterInkBulletConfig.SPLATTERSHOT.move(), 1.52);
        assertTrue(landing[0] > 11.5 && landing[0] < 13.5, "landed at " + landing[0]);
        assertTrue(landing[1] < 20, "took " + landing[1] + " frames to land");
    }

    @Test
    @DisplayName("shots are timed in frames: three ticks hold nine frames")
    void shotsAreTimedInFrames() {
        // every 6 frames: one shot every other tick, the first one right away
        int[] expected = {1, 0, 1, 0, 1, 0};
        for (int tick = 0; tick < expected.length; tick++) {
            assertEquals(expected[tick], SPLATTERSHOT.shotsAt(tick), "shots on tick " + tick);
        }

        // every 4 frames: 3 shots every 4 ticks, not one per tick
        var fast = new AutomaticShooterConfig(ShooterInkBulletConfig.SPLATTERSHOT, 4, 0, 0, 0, 0);
        int shots = 0;
        for (int tick = 0; tick < 40; tick++) {
            shots += fast.shotsAt(tick);
        }
        assertEquals(30, shots, "a weapon firing every 4 frames fires 15 times a second");
    }

    @Test
    @DisplayName("letting go does not make the next shot come sooner")
    void tappingIsNoFasterThanHolding() {
        // fired on tick 0, let go after it: 3 frames went by, and the next shot is due on frame 6, a tick later
        assertEquals(1, SPLATTERSHOT.cooldownAfter(1));
        // let go right when a shot is due: no wait
        assertEquals(0, SPLATTERSHOT.cooldownAfter(2));
    }

    @Test
    @DisplayName("damage falls off between the reduce frames")
    void damageFallsOff() {
        var damage = new BulletDamageParam(360, 180, 8, 40);
        assertEquals(7.2F, damage.damage(0), 1.0E-5);
        assertEquals(7.2F, damage.damage(8), 1.0E-5);
        assertEquals(5.4F, damage.damage(24), 1.0E-5);
        assertEquals(3.6F, damage.damage(40), 1.0E-5);
        assertEquals(3.6F, damage.damage(400), 1.0E-5);
    }

    @Test
    @DisplayName("droplets spread over the cycle, and the feet get some on the listed shots")
    void dropletsFollowTheSplashCycle() {
        var spawn = new SplashSpawnParam(9.2F, 1.2F, 1.5F, 8, List.of(4));
        int total = 0;
        for (int shot = 0; shot < 8; shot++) {
            int count = spawn.dropletCount(shot);
            assertTrue(count == 1 || count == 2, "shot " + shot + " dropped " + count);
            total += count;
        }
        assertEquals(12, total, "1.5 droplets per shot over 8 shots");

        for (int shot = 0; shot < 16; shot++) {
            int number = shot % 8 + 1;
            assertEquals(number == 4 || number == 8, spawn.dropsNearest(shot), "nearest droplet of shot " + number);
        }
        assertFalse(SplashSpawnParam.NONE.dropsNearest(1));
    }

    @Test
    @DisplayName("weapons survive being written as data, defaults left out")
    void weaponsRoundTrip() {
        CodecAssertions.assertJsonRoundTrip(ShooterInkBulletConfig.CODEC, ShooterInkBulletConfig.SPLATTERSHOT);
        CodecAssertions.assertJsonRoundTrip(ShooterInkBulletConfig.CODEC, ShooterInkBulletConfig.DOT_96_GAL);
        CodecAssertions.assertJsonRoundTrip(AutomaticShooterConfig.CODEC.codec(), SPLATTERSHOT);
        CodecAssertions.assertStreamRoundTrip(AutomaticShooterConfig.STREAM_CODEC, SPLATTERSHOT);
    }

    @Test
    @DisplayName("shooting slows walking down to the weapon's speed, never above walking")
    void walkingSpeedWhileFiring() {
        assertEquals(1.0F, SPLATTERSHOT.walkSpeedMultiplier(), 0.01F);
        var heavy = new AutomaticShooterConfig(ShooterInkBulletConfig.DOT_96_GAL, 12, 4, 11.3511F, 0.04F, 2);
        assertEquals(0.556F, heavy.walkSpeedMultiplier(), 0.01F);
    }
}
