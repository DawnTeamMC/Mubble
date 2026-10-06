package fr.hugman.mubble.splatoon.mixin;

import fr.hugman.mubble.splatoon.world.level.ink.InkSync;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerChunkSender.class)
public class PlayerChunkSenderMixin {
    /**
     * Sends the ink of a chunk right behind the chunk, so that the client always has the chunk when its ink arrives.
     */
    @Inject(method = "sendChunk", at = @At("TAIL"))
    private static void splatoon$sendInk(ServerGamePacketListenerImpl connection, ServerLevel level, LevelChunk chunk, CallbackInfo ci) {
        InkSync.sendChunk(connection.player, chunk);
    }
}
