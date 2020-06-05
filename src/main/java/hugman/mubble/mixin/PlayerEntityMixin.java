package hugman.mubble.mixin;

import hugman.mubble.init.MubbleSounds;
import hugman.mubble.objects.item.LightsaberItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin
{
	private static ItemStack prevStack;

	@Inject(method = "interact", at = @At(value = "TAIL"), cancellable = true)
	private void interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> info)
	{
		PlayerEntity player = (PlayerEntity) (Object) this;
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() == Items.CARROT && entity.getType() == EntityType.PUFFERFISH)
		{
			PufferfishEntity pufferfish = (PufferfishEntity) entity;
			if (pufferfish.getPuffState() >= 1 && pufferfish.isAlive())
			{
				if (!player.abilities.creativeMode)
				{
					stack.decrement(1);
				}
				player.swingHand(hand);
				pufferfish.playSound(MubbleSounds.ENTITY_PUFFERFISH_AEUGH, 0.6F, 1.0F);
				info.setReturnValue(ActionResult.SUCCESS);
			}
		}
	}

	@Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
	private void tick(CallbackInfo info)
	{
		PlayerEntity player = (PlayerEntity) (Object) this;
		World world = player.getEntityWorld();
		if (prevStack == null) prevStack = player.getMainHandStack();
		ItemStack currentStack = player.getMainHandStack();
		if (prevStack.getItem() != currentStack.getItem())
		{
			if (currentStack.getItem() instanceof LightsaberItem)
			{
				((LightsaberItem) currentStack.getItem()).onPullOut(player, world);
			}
			if (prevStack.getItem() instanceof LightsaberItem)
			{
				((LightsaberItem) prevStack.getItem()).onPullIn(player, world);
			}
		}
		prevStack = player.getMainHandStack();
	}
}
