package fr.hugman.mubble.super_mario.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags.*;
import static fr.hugman.mubble.super_mario.references.SuperMarioEntityTypeIds.*;
import static net.minecraft.world.entity.EntityTypeIds.*;

public class SuperMarioEntityTypeTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
	public SuperMarioEntityTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// Mod
		builder(KOOPA_SHELLS).add(GREEN_KOOPA_SHELL, RED_KOOPA_SHELL);
		builder(CAN_STOMP).add(PLAYER);
		builder(STOMPABLE).add(GOOMBA, GREEN_KOOPA_SHELL);

		// bosses shrug an ice ball off; every other mob is judged on its bulk alone
		builder(FREEZE_IMMUNE).add(ENDER_DRAGON, WITHER);

		// Vanilla
		builder(EntityTypeTags.DISMOUNTS_UNDERWATER).add(GOOMBA);
		builder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH).add(GOOMBA);
	}
}