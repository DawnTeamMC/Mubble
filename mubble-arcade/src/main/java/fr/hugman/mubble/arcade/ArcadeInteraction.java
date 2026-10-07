package fr.hugman.mubble.arcade;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

/**
 * What the hands may do under the arcade layer: nothing during a move flagged {@code hands_busy},
 * and whatever the {@link InteractionPolicy} of the profile allows the rest of the time. With the
 * orbit camera on, the hands reach out where the body faces, level with the horizon: they hit and
 * use items, and never mine, place nor use a block, whatever the policy.
 * <p>
 * The callbacks fire on both sides, so that a client never swings at something the server would
 * refuse anyway.
 */
public final class ArcadeInteraction {
    private ArcadeInteraction() {
    }

    public static void register() {
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hit) -> refused(player, Kind.ATTACK) ? InteractionResult.FAIL : InteractionResult.PASS);
        UseItemCallback.EVENT.register((player, level, hand) -> refused(player, Kind.USE_ITEM) ? InteractionResult.FAIL : InteractionResult.PASS);
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> refused(player, Kind.BLOCK) ? InteractionResult.FAIL : InteractionResult.PASS);
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> refused(player, Kind.BLOCK) ? InteractionResult.FAIL : InteractionResult.PASS);
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> refused(player, Kind.ENTITY) ? InteractionResult.FAIL : InteractionResult.PASS);
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> !refused(player, Kind.BLOCK));
    }

    /** Whether {@code player} may not do something of {@code kind} right now. */
    public static boolean refused(Player player, Kind kind) {
        var controller = ArcadeController.of(player);
        if (!controller.isDriving()) {
            return false;
        }
        if (controller.handsBusy()) {
            return true;
        }
        var policy = controller.interaction();
        return switch (kind) {
            case ATTACK -> !policy.allowsAttackingEntities();
            case USE_ITEM -> !policy.allowsUsingItems();
            case BLOCK -> !policy.allowsWorldInteraction() || controller.orbiting();
            case ENTITY -> !policy.allowsWorldInteraction();
        };
    }

    public enum Kind {
        /** Hitting an entity. */
        ATTACK,
        /** Using the item in hand on nothing in particular: a bow, food, a shield. */
        USE_ITEM,
        /** Mining, placing and using blocks. */
        BLOCK,
        /** Using an entity: trading, feeding, riding. */
        ENTITY
    }
}
