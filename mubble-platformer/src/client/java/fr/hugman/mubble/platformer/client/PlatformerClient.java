package fr.hugman.mubble.platformer.client;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.client.config.MubbleSettings;
import fr.hugman.mubble.platformer.client.animation.PlatformerAnimationLoader;
import fr.hugman.mubble.platformer.client.animation.PlatformerPlayerAnimator;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import fr.hugman.mubble.platformer.client.camera.PlatformerSilhouette;
import fr.hugman.mubble.platformer.client.hud.PlatformerDebugHud;
import fr.hugman.mubble.platformer.PlatformerLocalDriver;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import fr.hugman.mubble.client.keybind.PowerUpKeybindsHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;

/**
 * The client side of the platformer movement layer: input, prediction, camera, animations, cues and the
 * debug HUD.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PlatformerKeyMappings.init();
        PlatformerClientConfig.load();
        MubbleSettings.register(new PlatformerSettings());
        PlatformerSilhouette.init();
        PlatformerLocalDriver.Holder.instance = new LocalPlatformerDriver();
        PlatformerClientNetworking.register();
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(Mubble.id("platformer_animations"), new PlatformerAnimationLoader());
        HudElementRegistry.addLast(Mubble.id("platformer_debug"), PlatformerDebugHud::extractRenderState);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> PlatformerRecorder.registerCommands(dispatcher));
        PowerUpKeybindsHandler.BEFORE_TRIGGER.register(PlatformerCamera::beforePowerUp);
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            PlatformerDebugHud.tick(minecraft);
            PlatformerCamera.tick(minecraft);
            PlatformerPlayerAnimator.tickRemotePlayers(minecraft);
        });
    }
}
