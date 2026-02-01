package fr.hugman.mubble.client.network;

import fr.hugman.mubble.network.protocol.common.custom.CollectCollectiblePayload;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
                    if (!itemStack.isEmpty()) {
                        itemStack.shrink(payload.amount());
                    }
                    if (payload.particle().isPresent()) {
                        var particleCount = 5 + from.getRandom().nextInt(2);
                        for (int i = 0; i < particleCount; i++) {
                            level.addParticle(payload.particle().get(), from.getRandomX(0.5), from.getY() + (from.getBbHeight() / 2), from.getRandomZ(0.5), 0, 0, 0);
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
