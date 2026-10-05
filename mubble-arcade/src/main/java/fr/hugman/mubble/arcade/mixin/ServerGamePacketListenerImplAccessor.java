package fr.hugman.mubble.arcade.mixin;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerGamePacketListenerImpl.class)
public interface ServerGamePacketListenerImplAccessor {
    @Accessor("awaitingPositionFromClient")
    @Nullable
    Vec3 mubble$getAwaitingPositionFromClient();

    @Accessor("aboveGroundTickCount")
    int mubble$getAboveGroundTickCount();
}
