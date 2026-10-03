package fr.hugman.mubble.test.unit.arcade;

import fr.hugman.mubble.world.arcade.ArcadeCameraHints;
import fr.hugman.mubble.world.arcade.ArcadeCosts;
import fr.hugman.mubble.world.arcade.ArcadeFallDamage;
import fr.hugman.mubble.world.arcade.ArcadeGrace;
import fr.hugman.mubble.world.arcade.ArcadePhysics;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.ArcadeValidation;
import fr.hugman.mubble.world.arcade.InteractionPolicy;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import fr.hugman.mubble.world.arcade.move.MoveSettings;
import java.util.Map;
import java.util.Optional;

/**
 * Profiles built in code, for the tests that have no data pack to read the shipped ones from.
 */
public final class ArcadeTestProfiles {
    private ArcadeTestProfiles() {
    }

    public static ArcadeProfile withMoves(Map<ArcadeMove, MoveSettings> moves) {
        return new ArcadeProfile(
                new ArcadePhysics(
                        new ArcadePhysics.Ground(0.216D, 0.4D, 8.0D, 4.0D, 25.0D, 0.9D, 0.6D, 0.1D, 0.05D),
                        new ArcadePhysics.Air(0.99D, 1.0D, 12.0D, 12.0D, 0.03D),
                        new ArcadePhysics.Gravity(0.09D, 1.6D, 0.5D, 0.08D, 0.5D, 3.92D, 1.0D),
                        new ArcadePhysics.Slope(4, 1.0D),
                        new ArcadePhysics.Bounce(4.0D, 10.0D, 7.0D, 0.2D),
                        new ArcadePhysics.Effects(0.35D, 0.1D, 0.05D, 0.2D),
                        4.0D),
                new ArcadeGrace(3, 150, 150, 4, 0.3D, 0.5D, 8, 0.35D, 4, 1),
                InteractionPolicy.FULL,
                new ArcadeCosts(true, true),
                new ArcadeFallDamage(1.0D, 3.0D),
                ArcadeCameraHints.NEUTRAL,
                ArcadeValidation.DEFAULT,
                moves);
    }

    public static MoveSettings settings(Map<String, Double> params) {
        return new MoveSettings(0.05F, Optional.empty(), Optional.empty(), fr.hugman.mubble.world.arcade.cue.MoveCues.NONE, params);
    }
}
