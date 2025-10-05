package fr.hugman.mubble.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;

@Environment(EnvType.CLIENT)
public class MubbleSpriteManagers {
    public static void registerSpriteManagers(MinecraftClient client) {
        TextureManager textureManager = client.getTextureManager();
        PowerUpSpriteManager.INSTANCE = new PowerUpSpriteManager(textureManager);

        ((ReloadableResourceManagerImpl) client.getResourceManager()).registerReloader(PowerUpSpriteManager.INSTANCE);
    }


    public static void stopSpriteManagers() {
        PowerUpSpriteManager.INSTANCE.close();
    }
}
