package fr.hugman.mubble.item.spawn_egg;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public record VariantSpawnEggInfo(
		Text name,
		boolean onlyInSearch
) {
	public static final Codec<VariantSpawnEggInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TextCodecs.CODEC.fieldOf("name").forGetter(VariantSpawnEggInfo::name),
		Codec.BOOL.optionalFieldOf("search_only", false).forGetter(VariantSpawnEggInfo::onlyInSearch)
	).apply(instance, VariantSpawnEggInfo::new));

	public VariantSpawnEggInfo(Text name) {
		this(name, false);
	}
}
