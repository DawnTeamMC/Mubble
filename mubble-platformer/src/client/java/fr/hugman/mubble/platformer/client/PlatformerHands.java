package fr.hugman.mubble.platformer.client;

import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.Nullable;

/**
 * The hands while the platformer layer drives. They answer to the platformer attack and use keys, which stand
 * in for the vanilla ones, and share them with the moves as Cappy's buttons are in Super Mario
 * Odyssey: a press goes to the moves while crouch is held (a roll) or while the move keeps the hands
 * busy (a dive out of a ground pound, a roll boost), and to the item in hand the rest of the time, see
 * {@link PlatformerController#handsGoToMoves}.
 * <p>
 * A press given to the moves is kept from the hands until the key is let go, so that holding it does
 * not start eating or mining once the move is over.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerHands {
    private static boolean attackKept;
    private static boolean useKept;

    private PlatformerHands() {
    }

    /**
     * The platformer key standing in for the vanilla {@code key} right now: the platformer attack or use while
     * the layer drives, nothing otherwise.
     */
    @Nullable
    public static KeyMapping standIn(Minecraft minecraft, KeyMapping key) {
        var player = minecraft.player;
        if (player == null || !PlatformerController.of(player).isDriving()) {
            return null;
        }
        if (key == minecraft.options.keyAttack) {
            return PlatformerKeyMappings.ATTACK;
        }
        return key == minecraft.options.keyUse ? PlatformerKeyMappings.USE : null;
    }

    /**
     * Called for every click of an platformer attack or use vanilla is about to act on: whether it goes to
     * the moves instead, in which case it becomes an action press.
     */
    public static boolean takeForMoves(Minecraft minecraft, KeyMapping key) {
        var player = minecraft.player;
        if (player == null || !PlatformerController.of(player).handsGoToMoves(PlatformerClientInput.crouchHeld())) {
            return false;
        }
        PlatformerClientInput.pressAction();
        if (key == PlatformerKeyMappings.ATTACK) {
            attackKept = true;
        } else {
            useKept = true;
        }
        return true;
    }

    /** Whether {@code key} is kept from the hands, its last press having gone to the moves. */
    public static boolean kept(KeyMapping key) {
        return key == PlatformerKeyMappings.ATTACK && attackKept || key == PlatformerKeyMappings.USE && useKept;
    }

    /** Every frame: a key let go goes back to the hands. Returns the action bit while a kept key is held. */
    static int onFrame() {
        attackKept &= PlatformerKeyMappings.ATTACK.isDown();
        useKept &= PlatformerKeyMappings.USE.isDown();
        return attackKept || useKept ? PlatformerInputFrame.ACTION : 0;
    }

    static void reset() {
        attackKept = false;
        useKept = false;
    }
}
