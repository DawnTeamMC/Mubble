package fr.hugman.mubble.test.unit.platformer;

import fr.hugman.mubble.platformer.PlatformerCameraHints;
import fr.hugman.mubble.platformer.PlatformerCosts;
import fr.hugman.mubble.platformer.PlatformerFallDamage;
import fr.hugman.mubble.platformer.PlatformerGrace;
import fr.hugman.mubble.platformer.PlatformerPhysics;
import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.PlatformerValidation;
import fr.hugman.mubble.platformer.InteractionPolicy;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.MoveSettings;
import java.util.Map;
import java.util.Optional;

/**
 * Profiles built in code, for the tests that have no data pack to read the shipped ones from.
 */
public final class PlatformerTestProfiles {
    private PlatformerTestProfiles() {
    }

    public static PlatformerProfile withMoves(Map<PlatformerMove, MoveSettings> moves) {
        return new PlatformerProfile(
                new PlatformerPhysics(
                        new PlatformerPhysics.Ground(0.216D, 0.4D, 8.0D, 4.0D, 25.0D, 0.9D, 0.6D, 0.1D, 0.05D),
                        new PlatformerPhysics.Air(0.99D, 1.0D, 12.0D, 12.0D, 0.03D),
                        new PlatformerPhysics.Gravity(0.09D, 1.6D, 0.5D, 0.08D, 0.5D, 3.92D, 1.0D),
                        new PlatformerPhysics.Slope(4, 1.0D),
                        new PlatformerPhysics.Bounce(4.0D, 10.0D, 7.0D, 0.2D),
                        new PlatformerPhysics.Effects(0.1D, 0.05D, 0.2D),
                        4.0D),
                new PlatformerGrace(3, 150, 150, 4, 0.3D, 0.5D, 8, 0.35D, 4, 1),
                InteractionPolicy.FULL,
                new PlatformerCosts(true, true),
                new PlatformerFallDamage(1.0D, 3.0D),
                PlatformerCameraHints.NEUTRAL,
                PlatformerValidation.DEFAULT,
                moves);
    }

    public static MoveSettings settings(Map<String, Double> params) {
        return new MoveSettings(0.05F, Optional.empty(), Optional.empty(), fr.hugman.mubble.platformer.cue.MoveCues.NONE, params);
    }
}
