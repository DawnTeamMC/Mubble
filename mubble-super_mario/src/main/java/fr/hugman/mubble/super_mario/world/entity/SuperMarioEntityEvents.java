package fr.hugman.mubble.super_mario.world.entity;

import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys;
import fr.hugman.mubble.super_mario.world.level.storage.loot.SuperMarioBuiltInLootTables;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;

public class SuperMarioEntityEvents {
	public static void register() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if(source.getEntity() instanceof PowerUpHolder powerUpHolder && entity.level() instanceof ServerLevel serverLevel) {
				var powerUp = powerUpHolder.getPowerUp();
				if(powerUp.isPresent() && powerUp.get().is(SuperMarioPowerUpKeys.GOLD)) {
					entity.dropFromLootTable(serverLevel, source, true, SuperMarioBuiltInLootTables.GOLDEN_KILL, itemStack -> {
						entity.spawnAtLocation(serverLevel, itemStack);
					});
				}
			}
		});
	}
}
