package fr.hugman.mubble.client.network;

import fr.hugman.mubble.core.particles.MubbleParticleTypes;
import fr.hugman.mubble.network.protocol.common.custom.CollectCollectiblePayload;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.item.MubbleItems;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class CollectCollectiblePayloadReceiver implements ClientPlayNetworking.PlayPayloadHandler<CollectCollectiblePayload> {
    public static final CollectCollectiblePayloadReceiver INSTANCE = new CollectCollectiblePayloadReceiver();

    @Override
    public void receive(CollectCollectiblePayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            var level = context.client().level;
            Entity from = level.getEntity(payload.itemId());

            if (from != null) {
                if (from instanceof CollectibleEntity collectible) {
                    ItemStack itemStack = collectible.getItem();

                    //TODO: make customizable
                    ParticleOptions particle = null;
                    if(itemStack.is(MubbleItems.COIN)) {
                        particle = MubbleParticleTypes.COIN_SPARKLE;
                    }
                    if(itemStack.is(MubbleItems.RED_COIN)) {
                        particle = MubbleParticleTypes.RED_COIN_SPARKLE;
                    }
                    if(itemStack.is(MubbleItems.BLUE_COIN)) {
                        particle = MubbleParticleTypes.BLUE_COIN_SPARKLE;
                    }
                    if(itemStack.is(MubbleItems.FLOWER_COIN)) {
                        particle = MubbleParticleTypes.FLOWER_COIN_SPARKLE;
                    }

                    if (!itemStack.isEmpty()) {
                        itemStack.shrink(payload.amount());
                    }
                    if(particle != null) {
                        var particleCount = 5 + from.getRandom().nextInt(2);
                        for (int i = 0; i < particleCount; i++) {
                            level.addParticle(particle, from.getRandomX(0.5), from.getY() + (from.getBbHeight() / 2), from.getRandomZ(0.5), 0,0,0);
                        }
                    }

                    if (itemStack.isEmpty()) {
                        level.removeEntity(payload.itemId(), Entity.RemovalReason.DISCARDED);
                    }
                }
            }
        });
    }
}
