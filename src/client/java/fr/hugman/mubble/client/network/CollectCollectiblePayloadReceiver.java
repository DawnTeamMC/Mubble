package fr.hugman.mubble.client.network;

import fr.hugman.mubble.core.particles.MubbleParticleTypes;
import fr.hugman.mubble.network.protocol.common.custom.CollectCollectiblePayload;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CollectCollectiblePayloadReceiver implements ClientPlayNetworking.PlayPayloadHandler<CollectCollectiblePayload> {
    public static final CollectCollectiblePayloadReceiver INSTANCE = new CollectCollectiblePayloadReceiver();

    @Override
    public void receive(CollectCollectiblePayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            var level = context.client().level;
            Entity from = level.getEntity(payload.itemId());
            LivingEntity to = (LivingEntity)level.getEntity(payload.playerId());
            if (to == null) {
                to = context.client().player;
            }

            if (from != null) {
                //EntityRenderState itemState = context.client().getEntityRenderDispatcher().extractEntity(from, 1.0F);
                //context.client().particleEngine.add(new ItemPickupParticle(level, itemState, to, from.getDeltaMovement()));
                for (int i = 0; i < 4; i++) {
                    level.addParticle(MubbleParticleTypes.GOLD_SPARK, from.getX(), from.getY() + (from.getBbHeight() / 2), from.getZ(), 0,0,0);
                }
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
        });
    }
}
