package fr.hugman.mubble.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fr.hugman.mubble.world.level.block.CustomNoteBlockInstrument;
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
	 * Swaps the sound of a note block for a {@link CustomNoteBlockInstrument} when it sits on top of one of its
	 * blocks.
	 * <p>
	 * The vanilla instrument is only ever replaced when it comes from the block below: a mob head on top still wins,
	 * as it does over any vanilla instrument.
	 */
	@ModifyExpressionValue(method = "triggerEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/properties/NoteBlockInstrument;getSoundEvent()Lnet/minecraft/core/Holder;"))
	private Holder<SoundEvent> mubble$playCustomInstrument(Holder<SoundEvent> original, @Local NoteBlockInstrument instrument, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos) {
		if (instrument.worksAboveNoteBlock()) {
			return original;
		}
		return CustomNoteBlockInstrument.byBlockBelow(level.registryAccess(), level.getBlockState(pos.below()))
				.map(custom -> custom.value().sound())
				.orElse(original);
	}
}
