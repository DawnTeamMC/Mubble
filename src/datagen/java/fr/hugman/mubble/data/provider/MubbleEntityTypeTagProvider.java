package fr.hugman.mubble.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.entity.MubbleEntityTypes.*;
import static fr.hugman.mubble.tags.MubbleEntityTypeTags.*;
import static net.minecraft.world.entity.EntityType.*;

public class MubbleEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public MubbleEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// Mubble
		valueLookupBuilder(KOOPA_SHELLS).add(GREEN_KOOPA_SHELL, RED_KOOPA_SHELL);
        valueLookupBuilder(CAN_STOMP).add(PLAYER);
        valueLookupBuilder(STOMPABLE).add(GOOMBA, GREEN_KOOPA_SHELL);

		// Vanilla
		valueLookupBuilder(EntityTypeTags.DISMOUNTS_UNDERWATER).add(GOOMBA);
		valueLookupBuilder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH).add(GOOMBA);
	}
}