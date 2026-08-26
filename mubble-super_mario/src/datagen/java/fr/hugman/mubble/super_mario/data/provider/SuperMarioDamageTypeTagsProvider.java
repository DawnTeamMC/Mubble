package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.tags.SuperMarioDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class SuperMarioDamageTypeTagsProvider extends FabricTagsProvider<DamageType> {
	public SuperMarioDamageTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.DAMAGE_TYPE, registriesFuture);
	}

    protected TagAppender<DamageType> builder(TagKey<DamageType> tag) {
        return TagAppender.forBuilder(this.getOrCreateRawBuilder(tag));
    }

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
        // what an enderman blinks away from, and what projectile protection is worth anything against
        this.builder(DamageTypeTags.IS_PROJECTILE)
                .add(SuperMarioDamageTypeIds.KOOPA_SHELL)
                .add(SuperMarioDamageTypeIds.FIREBALL)
                .add(SuperMarioDamageTypeIds.ICEBALL)
                .add(SuperMarioDamageTypeIds.GOLD_FIREBALL);

        this.builder(SuperMarioDamageTypeTags.MELTS_FREEZE)
                // optional only because nothing here generates the vanilla tag for the validator to find
                .addOptionalTag(DamageTypeTags.IS_FIRE)
                .add(SuperMarioDamageTypeIds.FIREBALL)
                .add(SuperMarioDamageTypeIds.GOLD_FIREBALL);

        this.builder(SuperMarioDamageTypeTags.INSTANT_KILLS_GOOMBAS)
                .add(SuperMarioDamageTypeIds.STOMP)
                .add(SuperMarioDamageTypeIds.KOOPA_SHELL);
	}
}