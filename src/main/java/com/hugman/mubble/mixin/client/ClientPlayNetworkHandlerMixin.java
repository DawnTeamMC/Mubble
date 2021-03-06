package com.hugman.mubble.mixin.client;

import com.hugman.mubble.init.MubbleEntities;
import com.hugman.mubble.object.entity.FireballEntity;
import com.hugman.mubble.object.entity.IceballEntity;
import com.hugman.mubble.object.entity.KirbyBallEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;

	@Inject(method = "onEntitySpawn", at = @At(value = "HEAD"), cancellable = true)
	private void mubble_onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo info) {
		ClientPlayNetworkHandler cpnh = (ClientPlayNetworkHandler) (Object) this;
		MinecraftClient client = MinecraftClient.getInstance();
		NetworkThreadUtils.forceMainThread(packet, cpnh, client);
		double x = packet.getX();
		double y = packet.getY();
		double z = packet.getZ();
		EntityType<?> entityType = packet.getEntityTypeId();
		Entity entity = null;
		if(entityType == MubbleEntities.FIREBALL) {
			entity = new FireballEntity(this.world, x, y, z);
		}
		else if(entityType == MubbleEntities.ICEBALL) {
			entity = new IceballEntity(this.world, x, y, z);
		}
		else if(entityType == MubbleEntities.KIRBY_BALL) {
			entity = new KirbyBallEntity(this.world, x, y, z);
		}
		if(entity != null) {
			int i = packet.getId();
			entity.updateTrackedPosition(x, y, z);
			entity.pitch = (float) (packet.getPitch() * 360) / 256.0F;
			entity.yaw = (float) (packet.getYaw() * 360) / 256.0F;
			entity.setEntityId(i);
			entity.setUuid(packet.getUuid());
			this.world.addEntity(i, entity);
		}
	}
}
