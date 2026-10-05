package fr.hugman.mubble.arcade.client;

import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * Shares attack and use between the hands and the moves, as Cappy's buttons are in Super Mario
 * Odyssey: a press goes to the moves while crouch is held (a roll) or while the move keeps the hands
 * busy (a dive out of a ground pound, a roll boost), and to the item in hand the rest of the time, see
 * {@link ArcadeController#handsGoToMoves}. It is the same on a keyboard and on a controller, since
 * Controlify presses the vanilla keys.
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
     * Called for every click of a key vanilla is about to act on: whether the click of attack or use
     * goes to the moves instead, in which case it becomes an action press.
     */
    public static boolean takeForMoves(Minecraft minecraft, KeyMapping key) {
        boolean attack = key == minecraft.options.keyAttack;
        if (!attack && key != minecraft.options.keyUse) {
            return false;
        }
        var player = minecraft.player;
        if (player == null || !ArcadeController.of(player).handsGoToMoves(ArcadeClientInput.crouchHeld())) {
            return false;
        }
        ArcadeClientInput.pressAction();
        if (attack) {
            attackKept = true;
        } else {
            useKept = true;
        }
        return true;
    }

    /** Whether {@code key} is kept from the hands, its last press having gone to the moves. */
    public static boolean kept(Minecraft minecraft, KeyMapping key) {
        return key == minecraft.options.keyAttack && attackKept || key == minecraft.options.keyUse && useKept;
    }

    /** Every frame: a key let go goes back to the hands. Returns the action bit while a kept key is held. */
    static int onFrame(Minecraft minecraft) {
        attackKept &= minecraft.options.keyAttack.isDown();
        useKept &= minecraft.options.keyUse.isDown();
        return attackKept || useKept ? ArcadeInputFrame.ACTION : 0;
    }

    static void reset() {
        attackKept = false;
        useKept = false;
    }
}
