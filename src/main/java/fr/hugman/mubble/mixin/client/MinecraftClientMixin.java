package fr.hugman.mubble.mixin.client;

import fr.hugman.mubble.client.texture.MubbleSpriteManagers;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;reload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReload;", shift = At.Shift.BEFORE))
    private void mubble$appendSpriteManagers(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        //TODO: create event and register power ups texture manager with it
        MubbleSpriteManagers.registerSpriteManagers(client);
    }
}