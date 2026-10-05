package fr.hugman.mubble.arcade.client;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.client.animation.ArcadeAnimationLoader;
import fr.hugman.mubble.arcade.client.animation.ArcadePlayerAnimator;
import fr.hugman.mubble.arcade.client.camera.ArcadeCamera;
import fr.hugman.mubble.arcade.client.hud.ArcadeDebugHud;
import fr.hugman.mubble.arcade.ArcadeLocalDriver;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;

/**
 * The client side of the arcade movement layer: input, prediction, camera, animations, cues and the
 * debug HUD.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArcadeKeyMappings.init();
        ArcadeClientConfig.load();
        ArcadeLocalDriver.Holder.instance = new LocalArcadeDriver();
        ArcadeClientNetworking.register();
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(Mubble.id("arcade_animations"), new ArcadeAnimationLoader());
        HudElementRegistry.addLast(Mubble.id("arcade_debug"), ArcadeDebugHud::extractRenderState);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> ArcadeRecorder.registerCommands(dispatcher));
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            ArcadeDebugHud.tick(minecraft);
            ArcadeCamera.tick(minecraft);
            ArcadePlayerAnimator.tickRemotePlayers(minecraft);
        });
    }
}
