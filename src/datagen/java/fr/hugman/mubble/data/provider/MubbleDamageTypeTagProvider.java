package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.tags.MubbleDamageTypeTags;
import fr.hugman.mubble.world.entity.damage.MubbleDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import java.util.concurrent.CompletableFuture;

public class MubbleDamageTypeTagProvider extends FabricTagProvider<DamageType> {
	public MubbleDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.DAMAGE_TYPE, registriesFuture);
	}

    protected TagAppender<ResourceKey<DamageType>, DamageType> builder(TagKey<DamageType> tag) {
        return TagAppender.forBuilder(this.getOrCreateRawBuilder(tag));
    }

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.builder(DamageTypeTags.IS_PROJECTILE)
                .add(MubbleDamageTypes.KOOPA_SHELL);

        this.builder(MubbleDamageTypeTags.INSTANT_KILLS_GOOMBAS)
                .add(MubbleDamageTypes.STOMP)
                .add(MubbleDamageTypes.KOOPA_SHELL);
	}
}