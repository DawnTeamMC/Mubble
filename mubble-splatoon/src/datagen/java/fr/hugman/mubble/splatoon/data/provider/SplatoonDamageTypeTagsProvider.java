package fr.hugman.mubble.splatoon.data.provider;

import fr.hugman.mubble.splatoon.world.damagesource.SplatoonDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class SplatoonDamageTypeTagsProvider extends FabricTagsProvider<DamageType> {
    public SplatoonDamageTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    protected TagAppender<DamageType> builder(TagKey<DamageType> tag) {
        return TagAppender.forBuilder(this.getOrCreateRawBuilder(tag));
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // like in Splatoon: ink pushes nobody around, and every shot of a fast shooter counts
        // (not in minecraft:is_projectile, which Super Mario already writes: modules must not ship the same file)
        this.builder(DamageTypeTags.NO_KNOCKBACK).add(SplatoonDamageTypes.INK);
        this.builder(DamageTypeTags.BYPASSES_COOLDOWN).add(SplatoonDamageTypes.INK);
    }
}
