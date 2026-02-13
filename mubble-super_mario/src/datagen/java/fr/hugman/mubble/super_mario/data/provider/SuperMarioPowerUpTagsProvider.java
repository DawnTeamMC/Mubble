package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import static fr.hugman.mubble.tags.MubblePowerUpTags.*;
import static fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys.*;

import java.util.concurrent.CompletableFuture;

public class SuperMarioPowerUpTagsProvider extends FabricTagsProvider<PowerUp> {
	public SuperMarioPowerUpTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, MubbleRegistries.POWER_UP, registriesFuture);
	}

	protected TagAppender<ResourceKey<PowerUp>, PowerUp> builder(TagKey<PowerUp> tag) {
		return TagAppender.forBuilder(this.getOrCreateRawBuilder(tag));
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		this.builder(CAN_RUN_ON_WATER)
				.add(MINI);
	}
}