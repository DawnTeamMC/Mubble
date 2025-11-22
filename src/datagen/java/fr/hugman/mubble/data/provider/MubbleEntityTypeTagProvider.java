package fr.hugman.mubble.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.entity.MubbleEntityTypes.*;
import static fr.hugman.mubble.tag.MubbleEntityTypeTags.*;
import static net.minecraft.entity.EntityType.*;

public class MubbleEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public MubbleEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		// Mubble
		valueLookupBuilder(KOOPA_SHELLS).add(GREEN_KOOPA_SHELL, RED_KOOPA_SHELL);
        valueLookupBuilder(CAN_STOMP).add(PLAYER);
        valueLookupBuilder(STOMPABLE).add(GOOMBA).addTag(KOOPA_SHELLS);

		// Vanilla
		valueLookupBuilder(EntityTypeTags.DISMOUNTS_UNDERWATER).add(GOOMBA);
		valueLookupBuilder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH).add(GOOMBA);
	}
}