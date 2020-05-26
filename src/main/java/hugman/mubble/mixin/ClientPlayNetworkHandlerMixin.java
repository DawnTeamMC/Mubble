package hugman.mubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.objects.entity.FireballEntity;
import hugman.mubble.objects.entity.IceballEntity;
import hugman.mubble.objects.entity.KirbyBallEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.thread.ThreadExecutor;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
	@Shadow private ClientWorld world;
	
	@SuppressWarnings("rawtypes")
	@Inject(method = "onEntitySpawn", at = @At(value = "HEAD"), cancellable = true)
	private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci)
	{
		ClientPlayNetworkHandler cpnh = (ClientPlayNetworkHandler) (Object) this;
		MinecraftClient client = MinecraftClient.getInstance();
		NetworkThreadUtils.forceMainThread(packet, cpnh, (ThreadExecutor) client);
		double x = packet.getX();
		double y = packet.getY();
		double z = packet.getZ();
		EntityType<?> entityType = packet.getEntityTypeId();
		Object entity = null;
		if (entityType == MubbleEntities.FIREBALL)
		{
			entity = new FireballEntity(this.world, x, y, z);
		}
		else if (entityType == MubbleEntities.ICEBALL)
		{
			entity = new IceballEntity(this.world, x, y, z);
		}
		else if (entityType == MubbleEntities.KIRBY_BALL)
		{
			entity = new KirbyBallEntity(this.world, x, y, z);
		}
		if (entity != null) {
			int i = packet.getId();
			((Entity) entity).updateTrackedPosition(x, y, z);
			((Entity) entity).pitch = (float)(packet.getPitch() * 360) / 256.0F;
			((Entity) entity).yaw = (float)(packet.getYaw() * 360) / 256.0F;
			((Entity) entity).setEntityId(i);
			((Entity) entity).setUuid(packet.getUuid());
			this.world.addEntity(i, (Entity) entity);
		}
	}
}
