package fr.hugman.mubble.zelda.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

/**
 * What a loftwing helmet is worth to the bird wearing it, and what it costs. Heavier helmets soak
 * up more of a crash but make the bird slower to come round, which is the whole trade the player
 * makes when picking one.
 * <p>
 * Keeping the numbers in a component rather than in a switch means a data pack, or another module,
 * can add a helmet of its own without a line of code here.
 */
public record LoftwingHelmetComponent(float crashProtection, float turnSeconds) {
	/** What a bare-headed loftwing gets: no padding, and the sharpest turns it is capable of. */
	public static final LoftwingHelmetComponent BAREHEADED = new LoftwingHelmetComponent(0.0F, 0.4F);

	public static final Codec<LoftwingHelmetComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("crash_protection").forGetter(LoftwingHelmetComponent::crashProtection),
			ExtraCodecs.POSITIVE_FLOAT.fieldOf("turn_seconds").forGetter(LoftwingHelmetComponent::turnSeconds)
	).apply(instance, LoftwingHelmetComponent::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, LoftwingHelmetComponent> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT, LoftwingHelmetComponent::crashProtection,
			ByteBufCodecs.FLOAT, LoftwingHelmetComponent::turnSeconds,
			LoftwingHelmetComponent::new
	);

	/** How far the bird may come round in one tick, in degrees. */
	public float maxTurnPerTick() {
		return 90.0F / (this.turnSeconds * 20.0F);
	}
}
