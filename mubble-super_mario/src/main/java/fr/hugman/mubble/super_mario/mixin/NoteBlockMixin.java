package fr.hugman.mubble.super_mario.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fr.hugman.mubble.super_mario.world.level.block.MarioPaintInstrument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {
	/**
	 * Swaps the sound of a vanilla note block for a {@link MarioPaintInstrument} when it sits on top of one of their
	 * blocks.
	 * <p>
	 * Swapping the sound rather than adding instruments leaves the {@code instrument} block state alone, which
	 * vanilla clients and worlds would not know how to read. The instrument it holds is only ever ignored when it
	 * comes from the block below: a mob head on top still wins, as it does over any vanilla instrument.
	 */
	@ModifyExpressionValue(method = "triggerEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/properties/NoteBlockInstrument;getSoundEvent()Lnet/minecraft/core/Holder;"))
	private Holder<SoundEvent> super_mario$playMarioPaintInstrument(Holder<SoundEvent> original, @Local NoteBlockInstrument instrument, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos) {
		if (instrument.worksAboveNoteBlock()) {
			return original;
		}
		return MarioPaintInstrument.byBlockBelow(level.getBlockState(pos.below()))
				.map(MarioPaintInstrument::getSound)
				.orElse(original);
	}
}
