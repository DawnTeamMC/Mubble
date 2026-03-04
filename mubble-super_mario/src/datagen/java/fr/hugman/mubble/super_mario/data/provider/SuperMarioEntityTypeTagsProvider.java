package fr.hugman.mubble.super_mario.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags.*;
import static fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes.*;
import static net.minecraft.world.entity.EntityType.*;

public class SuperMarioEntityTypeTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
	public SuperMarioEntityTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// Mod
		valueLookupBuilder(KOOPA_SHELLS).add(GREEN_KOOPA_SHELL, RED_KOOPA_SHELL);
        valueLookupBuilder(CAN_STOMP).add(PLAYER);
        valueLookupBuilder(STOMPABLE).add(GOOMBA, GREEN_KOOPA_SHELL);

        // All super_mario entities
        valueLookupBuilder(ALL).add(GOOMBA, GREEN_KOOPA_SHELL, RED_KOOPA_SHELL, FIREBALL, ICEBALL, GOLD_FIREBALL, CLOUD_PLATFORM, BUBBLE);

        // Bubble trapping rules
        valueLookupBuilder(BUBBLE_CANNOT_TRAP).add(PLAYER);
        // bubble_can_trap is intentionally left empty - entities are added via data packs or other modules

		// Vanilla
		valueLookupBuilder(EntityTypeTags.DISMOUNTS_UNDERWATER).add(GOOMBA);
		valueLookupBuilder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH).add(GOOMBA);
	}
}