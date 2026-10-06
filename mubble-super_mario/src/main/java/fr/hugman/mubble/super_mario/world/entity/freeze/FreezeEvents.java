package fr.hugman.mubble.super_mario.world.entity.freeze;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;

/**
 * Cuts off everything a frozen player could otherwise reach out and do.
 * <p>
 * Being unable to move is not much of a punishment for someone who can still mine the block in front
 * of them, so the whole of mining, placing, using and hitting goes with it. The callbacks fire on the
 * client as much as on the server, which is what keeps a frozen player from watching a block crack
 * open on their own screen before the server tells them otherwise.
 *
 * @see Freezing
 */
public final class FreezeEvents {
    private FreezeEvents() {
    }

    public static void register() {
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> refuseWhileFrozen(player));
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> !Freezing.isFrozen(player));
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> refuseWhileFrozen(player));
        UseItemCallback.EVENT.register((player, level, hand) -> refuseWhileFrozen(player));
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> refuseWhileFrozen(player));
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> refuseWhileFrozen(player));
    }

    private static InteractionResult refuseWhileFrozen(Entity player) {
        return Freezing.isFrozen(player) ? InteractionResult.FAIL : InteractionResult.PASS;
    }
}
