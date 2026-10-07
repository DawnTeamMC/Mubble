package fr.hugman.mubble.arcade.client;

import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.Nullable;

/**
 * The hands while the arcade layer drives. They answer to the arcade attack and use keys, which stand
 * in for the vanilla ones, and share them with the moves as Cappy's buttons are in Super Mario
 * Odyssey: a press goes to the moves while crouch is held (a roll) or while the move keeps the hands
 * busy (a dive out of a ground pound, a roll boost), and to the item in hand the rest of the time, see
 * {@link ArcadeController#handsGoToMoves}.
 * <p>
 * A press given to the moves is kept from the hands until the key is let go, so that holding it does
 * not start eating or mining once the move is over.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeHands {
    private static boolean attackKept;
    private static boolean useKept;

    private ArcadeHands() {
    }

    /**
     * The arcade key standing in for the vanilla {@code key} right now: the arcade attack or use while
     * the layer drives, nothing otherwise.
     */
    @Nullable
    public static KeyMapping standIn(Minecraft minecraft, KeyMapping key) {
        var player = minecraft.player;
        if (player == null || !ArcadeController.of(player).isDriving()) {
            return null;
        }
        if (key == minecraft.options.keyAttack) {
            return ArcadeKeyMappings.ATTACK;
        }
        return key == minecraft.options.keyUse ? ArcadeKeyMappings.USE : null;
    }

    /**
     * Called for every click of an arcade attack or use vanilla is about to act on: whether it goes to
     * the moves instead, in which case it becomes an action press.
     */
    public static boolean takeForMoves(Minecraft minecraft, KeyMapping key) {
        var player = minecraft.player;
        if (player == null || !ArcadeController.of(player).handsGoToMoves(ArcadeClientInput.crouchHeld())) {
            return false;
        }
        ArcadeClientInput.pressAction();
        if (key == ArcadeKeyMappings.ATTACK) {
            attackKept = true;
        } else {
            useKept = true;
        }
        return true;
    }

    /** Whether {@code key} is kept from the hands, its last press having gone to the moves. */
    public static boolean kept(KeyMapping key) {
        return key == ArcadeKeyMappings.ATTACK && attackKept || key == ArcadeKeyMappings.USE && useKept;
    }

    /** Every frame: a key let go goes back to the hands. Returns the action bit while a kept key is held. */
    static int onFrame() {
        attackKept &= ArcadeKeyMappings.ATTACK.isDown();
        useKept &= ArcadeKeyMappings.USE.isDown();
        return attackKept || useKept ? ArcadeInputFrame.ACTION : 0;
    }

    static void reset() {
        attackKept = false;
        useKept = false;
    }
}
