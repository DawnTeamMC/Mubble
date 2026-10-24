package fr.hugman.mubble.platformer.client.cue;

import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.PlatformerPlayer;
import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.PlatformerProfiles;
import fr.hugman.mubble.platformer.client.compat.PlatformerControllerBindings;
import fr.hugman.mubble.platformer.cue.Cue;
import fr.hugman.mubble.platformer.cue.CueEvent;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.sim.MoveContext;
import fr.hugman.mubble.platformer.sim.MoveEvent;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

/**
 * Plays the sounds and particles of the platformer moves, on the client only: every client plays them
 * for the players it sees, its own one from its predicted steps and the others from their synced
 * moves.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerCuePlayer {
    private PlatformerCuePlayer() {
    }

    /** Plays the cues of the events of a predicted step, rumbles of the controller included: the step is the player's own. */
    public static void play(Player player, PlatformerProfile profile, List<MoveEvent> events) {
        for (var event : events) {
            profile.settingsOrDefault(event.move()).cues().get(event.type()).ifPresent(cue -> {
                play(player, event.move(), cue, event.intensity());
                cue.rumble().ifPresent(rumble -> rumble(rumble, event.intensity()));
            });
        }
    }

    private static void rumble(Cue.Rumble rumble, double intensity) {
        var controller = PlatformerControllerBindings.Holder.instance;
        float scale = rumble.scale(intensity);
        if (controller != null && scale > 0.0F) {
            controller.rumble(rumble.strong() * scale, rumble.weak() * scale, rumble.ticks());
        }
    }

    /** Plays one cue of {@code move}, under {@code profile} or, when unknown, the profile the player shows. */
    public static void playOne(Player player, @Nullable PlatformerProfile profile, PlatformerMove move, CueEvent event, double intensity) {
        var resolved = profile != null ? profile : profileOf(player);
        if (resolved != null) {
            playCue(player, resolved, move, event, intensity);
        }
    }

    /** Plays the tick cue of {@code move}, if it is due after {@code ticks} ticks in the move. */
    public static void playTick(Player player, @Nullable PlatformerProfile profile, PlatformerMove move, int ticks) {
        var resolved = profile != null ? profile : profileOf(player);
        if (resolved == null) {
            return;
        }
        resolved.settingsOrDefault(move).cues().get(CueEvent.TICK).ifPresent(cue -> {
            if (ticks % cue.interval() == 0) {
                play(player, move, cue, player.getDeltaMovement().horizontalDistance());
            }
        });
    }

    @Nullable
    private static PlatformerProfile profileOf(Player player) {
        var controller = PlatformerController.of(player);
        if (controller.profile() != null) {
            return controller.profile();
        }
        var key = ((PlatformerPlayer) player).mubble$platformerVisual().profile();
        return key == null ? null : PlatformerProfiles.get(player.level(), key).orElse(null);
    }

    private static void playCue(Player player, PlatformerProfile profile, PlatformerMove move, CueEvent event, double intensity) {
        profile.settingsOrDefault(move).cues().get(event).ifPresent(cue -> play(player, move, cue, intensity));
    }

    private static void play(Player player, PlatformerMove move, Cue cue, double intensity) {
        var level = player.level();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        cue.sound().ifPresent(sound -> level.playLocalSound(x, y, z, sound.value(), SoundSource.PLAYERS, cue.volume(), cue.pitch(), false));

        int count = cue.particleCount(intensity);
        if (count <= 0) {
            return;
        }
        ParticleOptions particle;
        if (cue.surface()) {
            var surface = surfaceOf(player, move);
            var state = level.getBlockState(surface);
            if (state.isAir()) {
                return;
            }
            particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
        } else if (cue.particle().isPresent()) {
            particle = cue.particle().get();
        } else {
            return;
        }

        var random = player.getRandom();
        var velocity = player.getDeltaMovement();
        for (int i = 0; i < count; i++) {
            switch (cue.shape()) {
                case RING -> {
                    double angle = (Math.PI * 2.0D) * i / count;
                    double dx = Mth.cos((float) angle);
                    double dz = Mth.sin((float) angle);
                    level.addParticle(particle, x + dx * cue.spread(), y + 0.1D, z + dz * cue.spread(), dx * cue.speed(), 0.02D, dz * cue.speed());
                }
                case TRAIL -> level.addParticle(particle,
                        x - velocity.x + (random.nextDouble() - 0.5D) * cue.spread(),
                        y + random.nextDouble() * 0.3D,
                        z - velocity.z + (random.nextDouble() - 0.5D) * cue.spread(),
                        -velocity.x * cue.speed(), cue.speed() * 0.5D, -velocity.z * cue.speed());
                case BURST -> level.addParticle(particle,
                        x + (random.nextDouble() - 0.5D) * cue.spread() * 2.0D,
                        y + random.nextDouble() * 0.2D,
                        z + (random.nextDouble() - 0.5D) * cue.spread() * 2.0D,
                        (random.nextDouble() - 0.5D) * cue.speed() * 2.0D,
                        random.nextDouble() * cue.speed(),
                        (random.nextDouble() - 0.5D) * cue.speed() * 2.0D);
            }
        }
    }

    /** The block the move touches: the wall behind a player sliding down it, the ground otherwise. */
    private static BlockPos surfaceOf(Player player, PlatformerMove move) {
        if (move.kind() == PlatformerMove.Kind.ATTACHED) {
            float facing = player.getYRot();
            double reach = player.getBbWidth() * 0.5D + 0.2D;
            return BlockPos.containing(player.getX() - MoveContext.directionX(facing) * reach, player.getY() + 1.0D, player.getZ() - MoveContext.directionZ(facing) * reach);
        }
        return BlockPos.containing(player.getX(), player.getY() - 0.2D, player.getZ());
    }
}
