package fr.hugman.mubble.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.item.spawn_egg.VariantSpawnEggInfo;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public record GoombaVariant(
		Optional<Component> name,
		GoombaAssetInfo assetInfo,
		Map<Holder<Attribute>, Double> baseAttributes,
		Optional<VariantSpawnEggInfo> spawnEggInfo
) {
	public static final Codec<GoombaVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(GoombaVariant::name),
			GoombaAssetInfo.CODEC.fieldOf("assets").forGetter(GoombaVariant::assetInfo),
			Codec.unboundedMap(Attribute.CODEC, Codec.DOUBLE).optionalFieldOf("base_attribute_values", Map.of()).forGetter(GoombaVariant::baseAttributes),
			VariantSpawnEggInfo.CODEC.optionalFieldOf("spawn_egg").forGetter(GoombaVariant::spawnEggInfo)
	).apply(instance, GoombaVariant::new));

	public static final Codec<Holder<GoombaVariant>> ENTRY_CODEC = RegistryFixedCodec.create(MubbleRegistryKeys.GOOMBA_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<GoombaVariant>> ENTRY_PACKET_CODEC = ByteBufCodecs.holderRegistry(MubbleRegistryKeys.GOOMBA_VARIANT);

	public void applyAttributes(LivingEntity livingEntity) {
		baseAttributes.forEach((attribute, value) -> livingEntity.getAttribute(attribute).setBaseValue(value));
	}

	public record GoombaAssetInfo(ClientAsset.ResourceTexture texture, ClientAsset.ResourceTexture surprised) {
		public static final Codec<GoombaAssetInfo> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						ClientAsset.ResourceTexture.CODEC.fieldOf("texture").forGetter(GoombaAssetInfo::texture),
						ClientAsset.ResourceTexture.CODEC.fieldOf("surprised").forGetter(GoombaAssetInfo::surprised)
				).apply(instance, GoombaAssetInfo::new));
	}
}
