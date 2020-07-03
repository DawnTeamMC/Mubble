package hugman.mubble.object.event;

import hugman.mubble.object.item.LightsaberItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class LightsaberEvents {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(event ->
		{
			if(LightsaberItem.idleTimer <= 95) {
				LightsaberItem.idleTimer++;
			}
			else {
				LightsaberItem.idleTimer = 0;
			}
		});
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->
		{
			ItemStack stack = player.getMainHandStack();
			if(stack.getItem() instanceof LightsaberItem) {
				((LightsaberItem) stack.getItem()).onSwing(player, entity.isAttackable() && !entity.isInvulnerableTo(DamageSource.player(player)) && entity.isAlive());
			}
			return ActionResult.PASS;
		});
	}
}
