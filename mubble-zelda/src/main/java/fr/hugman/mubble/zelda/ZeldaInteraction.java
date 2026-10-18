package fr.hugman.mubble.zelda;

import fr.hugman.mubble.zelda.tags.ZeldaBlockTags;
import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

/**
 * What the Zelda module lets you do with your hands. For now: holding a glass bottle to a mushroom,
 * which is how Orielle heals her loftwing in Skyward Sword.
 */
public final class ZeldaInteraction {
	private ZeldaInteraction() {
	}

	public static void register() {
		UseBlockCallback.EVENT.register(ZeldaInteraction::fillBottleWithSpores);
	}

	private static InteractionResult fillBottleWithSpores(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.is(Items.GLASS_BOTTLE)) {
			return InteractionResult.PASS;
		}
		BlockPos pos = hit.getBlockPos();
		if (!level.getBlockState(pos).is(ZeldaBlockTags.SPORE_SOURCES)) {
			return InteractionResult.PASS;
		}
		// The mushroom goes into the bottle, cap and all: they grow back readily enough.
		level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
		if (!level.isClientSide()) {
			level.destroyBlock(pos, false);
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ZeldaItems.MUSHROOM_SPORES_BOTTLE)));
		}
		return InteractionResult.SUCCESS;
	}
}
