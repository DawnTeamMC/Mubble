package fr.hugman.mubble.super_mario.network.syncher;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.GoombaVariant;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

public class SuperMarioEntityDataSerializers {
    public static final EntityDataSerializer<Holder<GoombaVariant>> GOOMBA_VARIANT = register("goomba_variant", GoombaVariant.STREAM_CODEC);

	public static <T> EntityDataSerializer<T> register(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		var handler = EntityDataSerializer.forValueType(codec);
		FabricEntityDataRegistry.register(SuperMario.id(name), handler);
		return handler;
	}
}
