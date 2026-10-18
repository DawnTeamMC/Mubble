package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.item.spawn_egg.VariantSpawnEggInfo;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Optional;

/**
 * A breed of loftwing. Only the look changes; what sets the breeds apart in play is how often they
 * turn up, which {@link #weight} decides.
 */
public record LoftwingVariant(
		Optional<Component> name,
		LoftwingAssetInfo assetInfo,
		int weight,
		Optional<VariantSpawnEggInfo> spawnEggInfo
) {
	public static final int DEFAULT_WEIGHT = 20;

	public static final Codec<LoftwingVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(LoftwingVariant::name),
			LoftwingAssetInfo.CODEC.fieldOf("assets").forGetter(LoftwingVariant::assetInfo),
			ExtraCodecs.POSITIVE_INT.optionalFieldOf("weight", DEFAULT_WEIGHT).forGetter(LoftwingVariant::weight),
			VariantSpawnEggInfo.CODEC.optionalFieldOf("spawn_egg").forGetter(LoftwingVariant::spawnEggInfo)
	).apply(instance, LoftwingVariant::new));

	public static final Codec<Holder<LoftwingVariant>> CODEC = RegistryFixedCodec.create(ZeldaRegistries.LOFTWING_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<LoftwingVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ZeldaRegistries.LOFTWING_VARIANT);

	/**
	 * Draws a breed at random, each one as likely as its weight. This is what keeps the crimson
	 * loftwing the rarity the people of Skyloft took for extinct.
	 */
	public static Optional<Holder.Reference<LoftwingVariant>> pickAtRandom(HolderLookup.RegistryLookup<LoftwingVariant> lookup, RandomSource random) {
		List<Holder.Reference<LoftwingVariant>> variants = lookup.listElements().toList();
		int total = variants.stream().mapToInt(variant -> variant.value().weight()).sum();
		if (total <= 0) {
			return variants.isEmpty() ? Optional.empty() : Optional.of(variants.getFirst());
		}
		int roll = random.nextInt(total);
		for (Holder.Reference<LoftwingVariant> variant : variants) {
			roll -= variant.value().weight();
			if (roll < 0) {
				return Optional.of(variant);
			}
		}
		return Optional.of(variants.getLast());
	}

	public record LoftwingAssetInfo(ClientAsset.ResourceTexture texture) {
		public static final Codec<LoftwingAssetInfo> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						ClientAsset.ResourceTexture.CODEC.fieldOf("texture").forGetter(LoftwingAssetInfo::texture)
				).apply(instance, LoftwingAssetInfo::new));
	}
}
