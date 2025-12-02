package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.entity.damage.MubbleDamageTypes;
import fr.hugman.mubble.tag.MubbleDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class MubbleDamageTypeTagProvider extends FabricTagProvider<DamageType> {
	public MubbleDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
	}

    protected ProvidedTagBuilder<RegistryKey<DamageType>, DamageType> builder(TagKey<DamageType> tag) {
        return ProvidedTagBuilder.of(this.getTagBuilder(tag));
    }

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.builder(DamageTypeTags.IS_PROJECTILE)
                .add(MubbleDamageTypes.KOOPA_SHELL);

        this.builder(MubbleDamageTypeTags.INSTANT_KILLS_GOOMBAS)
                .add(MubbleDamageTypes.STOMP)
                .add(MubbleDamageTypes.KOOPA_SHELL);
	}
}