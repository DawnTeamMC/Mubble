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
 * and whatever the {@link InteractionPolicy} of the profile allows the rest of the time.
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
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> refused(player, Kind.WORLD) ? InteractionResult.FAIL : InteractionResult.PASS);
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> refused(player, Kind.WORLD) ? InteractionResult.FAIL : InteractionResult.PASS);
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> refused(player, Kind.WORLD) ? InteractionResult.FAIL : InteractionResult.PASS);
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> !refused(player, Kind.WORLD));
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
            case WORLD -> !policy.allowsWorldInteraction();
        };
    }

    public enum Kind {
        /** Hitting an entity. */
        ATTACK,
        /** Using the item in hand on nothing in particular: a bow, food, a shield. */
        USE_ITEM,
        /** Mining, placing, and using blocks or entities. */
        WORLD
    }
}
