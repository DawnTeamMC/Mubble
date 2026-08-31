package fr.hugman.mubble.super_mario.client;

import fr.hugman.mubble.super_mario.client.gui.screens.inventory.BumpableScreen;
import fr.hugman.mubble.super_mario.client.keybind.FreezeStruggleHandler;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.client.particle.SuperMarioParticleResources;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderPipelines;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderTypes;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderers;
import com.google.common.reflect.Reflection;
import fr.hugman.mubble.super_mario.world.inventory.SuperMarioMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;

@Environment(EnvType.CLIENT)
public class SuperMarioClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(SuperMarioModelLayers.class);
        Reflection.initialize(SuperMarioRenderPipelines.class);
        Reflection.initialize(SuperMarioRenderTypes.class);

        registerHandledScreens();
        SuperMarioRenderers.registerEntities();
        SuperMarioRenderers.registerBlockEntities();
        SuperMarioParticleResources.register();
        ClientTickEvents.END_CLIENT_TICK.register(FreezeStruggleHandler::tick);
    }

    private static void registerHandledScreens() {
        MenuScreens.register(SuperMarioMenuTypes.BUMPABLE_BLOCK, BumpableScreen::new);
    }
}
