package fr.hugman.mubble.world.voyage;

import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import fr.hugman.mubble.world.voyage.trial.TrialPlatform;
import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

/**
 * What every node of a voyage has, whichever kind it is.
 *
 * <p>A trial and a waystation are the same three things — a name, a look and a floor — and differ
 * only in what they mean: a trial is something you complete, a waystation is somewhere you stand
 * between them. Everything that opens a level and drops a player into it works through this, so
 * neither kind needs its own copy of that.
 *
 * <p>They stay separate records rather than one with a flag, because a trial is where objectives and
 * rulesets land and a waystation is where a shop lands. The moment either arrives they stop having
 * the same shape.
 */
public interface VoyageNodeContent {
    /** What the player is told they have arrived at. */
    Component displayName();

    /** The look, resolved against this node's seed on entry. */
    Holder<EnvironmentProfile> environment();

    /** The ground to build, since node levels generate empty. */
    TrialPlatform platform();

    /**
     * {@return the clock time this node's level should be parked at, if its environment names one}
     *
     * <p>Reached through the environment because that is where a data pack declares it, but it is
     * not an attribute and cannot be applied as a layer: clocks in 26.2 hang off the server, not the
     * level, so the only per-node way to set one is at level creation.
     */
    default Optional<Integer> fixedTime() {
        return this.environment().value().fixedTime();
    }
}
