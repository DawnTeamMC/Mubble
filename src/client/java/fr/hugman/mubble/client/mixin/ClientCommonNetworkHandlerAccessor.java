package fr.hugman.mubble.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientCommonPacketListenerImpl.class)
public interface ClientCommonNetworkHandlerAccessor {
    @Accessor("minecraft")
    Minecraft getClient();
}