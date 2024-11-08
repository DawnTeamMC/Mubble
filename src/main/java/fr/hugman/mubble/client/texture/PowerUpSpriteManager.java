package fr.hugman.mubble.client.texture;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.power_up.PowerUp;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PowerUpSpriteManager extends SpriteAtlasHolder implements IdentifiableResourceReloadListener {
    public static PowerUpSpriteManager INSTANCE = null;

    public PowerUpSpriteManager(TextureManager textureManager) {
        super(textureManager, Mubble.id("textures/atlas/power_ups.png"), Mubble.id("power_ups"));
    }

    public Sprite getSprite(RegistryEntry<PowerUp> powerUp) {
        return this.getSprite(PowerUp.getSpriteId(powerUp));
    }

    @Override
    public Identifier getFabricId() {
        return Mubble.id("power_ups");
    }
}
