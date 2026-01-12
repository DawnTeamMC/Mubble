package fr.hugman.mubble.client.network;

import fr.hugman.mubble.core.particles.MubbleParticleTypes;
import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class MubbleClientReceivers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.POWER_UP_CHANGE, ((payload, context) -> context.client().execute(() -> {
            PowerUp.onChange(context.player(), payload.previous(), payload.next());
        })));
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.COLLECT_COLLECTIBLE, ((payload, context) -> context.client().execute(() -> {
            var level = context.client().level;
            Entity from = level.getEntity(payload.itemId());
            LivingEntity to = (LivingEntity)level.getEntity(payload.playerId());
            if (to == null) {
                to = context.client().player;
            }

            if (from != null) {
                //EntityRenderState itemState = context.client().getEntityRenderDispatcher().extractEntity(from, 1.0F);
                //context.client().particleEngine.add(new ItemPickupParticle(level, itemState, to, from.getDeltaMovement()));
                level.addParticle(MubbleParticleTypes.GOLD_SPARK, from.getX(), from.getY() + (from.getBbHeight() / 2), from.getZ(), 0,0,0);
                if (from instanceof CollectibleEntity collectible) {
                    ItemStack itemStack = collectible.getItem();
                    if (!itemStack.isEmpty()) {
                        itemStack.shrink(payload.amount());
                    }

                    if (itemStack.isEmpty()) {
                        level.removeEntity(payload.itemId(), Entity.RemovalReason.DISCARDED);
                    }
                }
            }
        })));
    }
}
