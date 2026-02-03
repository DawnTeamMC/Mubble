package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeKeys;
import fr.hugman.mubble.super_mario.tags.SuperMarioDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class SuperMarioDamageTypeTagsProvider extends FabricTagsProvider<DamageType> {
	public SuperMarioDamageTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.DAMAGE_TYPE, registriesFuture);
	}

    protected TagAppender<ResourceKey<DamageType>, DamageType> builder(TagKey<DamageType> tag) {
        return TagAppender.forBuilder(this.getOrCreateRawBuilder(tag));
    }

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.builder(DamageTypeTags.IS_PROJECTILE)
                .add(SuperMarioDamageTypeKeys.KOOPA_SHELL);

        this.builder(SuperMarioDamageTypeTags.INSTANT_KILLS_GOOMBAS)
                .add(SuperMarioDamageTypeKeys.STOMP)
                .add(SuperMarioDamageTypeKeys.KOOPA_SHELL);
	}
}