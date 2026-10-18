package fr.hugman.mubble.zelda.network.syncher;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingVariant;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

public class ZeldaEntityDataSerializers {
    public static final EntityDataSerializer<Holder<LoftwingVariant>> LOFTWING_VARIANT = register("loftwing_variant", LoftwingVariant.STREAM_CODEC);

    public static <T> EntityDataSerializer<T> register(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        var handler = EntityDataSerializer.forValueType(codec);
        FabricEntityDataRegistry.register(Zelda.id(name), handler);
        return handler;
    }
}
