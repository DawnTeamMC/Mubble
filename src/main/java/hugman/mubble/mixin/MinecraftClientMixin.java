package hugman.mubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hugman.mubble.objects.item.LightsaberItem;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
	private void tick(CallbackInfo ci)
	{
		if(LightsaberItem.idleTimer <= 95)
		{
			LightsaberItem.idleTimer++;
		}
		else
		{
			LightsaberItem.idleTimer = 0;
		}
	}
}
