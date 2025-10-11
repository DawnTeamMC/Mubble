package fr.hugman.mubble.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.item.spawn_egg.VariantSpawnEggInfo;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.AssetInfo;

import java.util.Map;
import java.util.Optional;

public record GoombaVariant(
		Optional<Text> name,
		GoombaAssetInfo assetInfo,
		Map<RegistryEntry<EntityAttribute>, Double> baseAttributes,
		Optional<VariantSpawnEggInfo> spawnEggInfo
) {
	public static final Codec<GoombaVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TextCodecs.CODEC.optionalFieldOf("name").forGetter(GoombaVariant::name),
			GoombaAssetInfo.CODEC.fieldOf("assets").forGetter(GoombaVariant::assetInfo),
			Codec.unboundedMap(EntityAttribute.CODEC, Codec.DOUBLE).optionalFieldOf("base_attribute_values", Map.of()).forGetter(GoombaVariant::baseAttributes),
			VariantSpawnEggInfo.CODEC.optionalFieldOf("spawn_egg").forGetter(GoombaVariant::spawnEggInfo)
	).apply(instance, GoombaVariant::new));

	public static final Codec<RegistryEntry<GoombaVariant>> ENTRY_CODEC = RegistryFixedCodec.of(MubbleRegistryKeys.GOOMBA_VARIANT);
	public static final PacketCodec<RegistryByteBuf, RegistryEntry<GoombaVariant>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(MubbleRegistryKeys.GOOMBA_VARIANT);

	public void applyAttributes(LivingEntity livingEntity) {
		baseAttributes.forEach((attribute, value) -> livingEntity.getAttributeInstance(attribute).setBaseValue(value));
	}

	public record GoombaAssetInfo(AssetInfo.TextureAssetInfo texture, AssetInfo.TextureAssetInfo surprised) {
		public static final Codec<GoombaAssetInfo> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						AssetInfo.TextureAssetInfo.CODEC.fieldOf("texture").forGetter(GoombaAssetInfo::texture),
						AssetInfo.TextureAssetInfo.CODEC.fieldOf("surprised").forGetter(GoombaAssetInfo::surprised)
				).apply(instance, GoombaAssetInfo::new));
	}
}
