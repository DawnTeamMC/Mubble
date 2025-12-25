package fr.hugman.mubble.world.item.spawn_egg;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record VariantSpawnEggInfo(
		Component name,
		boolean onlyInSearch
) {
	public static final Codec<VariantSpawnEggInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ComponentSerialization.CODEC.fieldOf("name").forGetter(VariantSpawnEggInfo::name),
		Codec.BOOL.optionalFieldOf("search_only", false).forGetter(VariantSpawnEggInfo::onlyInSearch)
	).apply(instance, VariantSpawnEggInfo::new));

	public VariantSpawnEggInfo(Component name) {
		this(name, false);
	}
}
