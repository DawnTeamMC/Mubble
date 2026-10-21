package fr.hugman.mubble.zelda.world.item;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Two fingers in the mouth and the sky answers. Blow it and your loftwing comes, wherever it was,
 * and if you happen to be falling at the time it slides in underneath you — which is the whole
 * point of owning one.
 */
public class LoftwingWhistleItem extends Item {
	public LoftwingWhistleItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), ZeldaSounds.LOFTWING_WHISTLE, SoundSource.PLAYERS, 1.0F, 1.0F);

		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}

		List<? extends Loftwing> answering = serverLevel.getEntities(ZeldaEntityTypes.LOFTWING, loftwing -> loftwing.isOwnedBy(player));
		boolean anyAnswered = false;
		boolean anyOutOfEarshot = false;
		for (Loftwing loftwing : answering) {
			// A whistle is a sound before it is a command: a bird too far off never hears it, and
			// whatever else it was doing at the time never comes into it.
			if (!loftwing.canHear(player)) {
				anyOutOfEarshot = true;
				continue;
			}
			anyAnswered |= loftwing.answerCall(player);
		}
		if (!anyAnswered) {
			// Out of earshot is the one silence worth telling apart, since it is the one the player
			// can do something about. Every other — chunks the bird is not loaded in, a lead
			// holding it, a command telling it to stay put — is the same shrug.
			tell(player, anyOutOfEarshot ? "too_far" : "no_answer");
			return InteractionResult.CONSUME;
		}
		player.getCooldowns().addCooldown(stack, WHISTLE_COOLDOWN_TICKS);
		return InteractionResult.SUCCESS;
	}

	/** Above the action bar, where a message about a bird that is not coming belongs. */
	private static void tell(Player player, String reason) {
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.sendSystemMessage(Component.translatable("item." + Zelda.MOD_ID + ".loftwing_whistle." + reason), true);
		}
	}

	/** Long enough that the whistle is a call, not a steering wheel. */
	private static final int WHISTLE_COOLDOWN_TICKS = 60;
}
