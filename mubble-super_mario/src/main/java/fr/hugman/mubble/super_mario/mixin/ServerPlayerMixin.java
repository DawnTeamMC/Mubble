package fr.hugman.mubble.super_mario.mixin;

import fr.hugman.mubble.super_mario.references.SuperMarioFlightRefillIds;
import fr.hugman.mubble.super_mario.tags.SuperMarioItemTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    /** Every way of picking an item up, collectibles included, ends on this statistic. */
    @Inject(method = "awardStat", at = @At("HEAD"))
    private void super_mario$refillFlightOnCoins(Stat<?> stat, int amount, CallbackInfo ci) {
        if (stat.getType() == Stats.ITEM_PICKED_UP && amount > 0 && stat.getValue() instanceof Item item && item.builtInRegistryHolder().is(SuperMarioItemTags.COINS)) {
            ((ServerPlayer) (Object) this).refillFlight(SuperMarioFlightRefillIds.COIN);
        }
    }
}
