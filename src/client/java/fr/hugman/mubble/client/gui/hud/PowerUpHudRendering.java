package fr.hugman.mubble.client.gui.hud;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.client.texture.PowerUpSpriteManager;
import fr.hugman.mubble.power_up.PowerUpHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class PowerUpHudRendering {
    private static final int OFFSET_FROM_SCREEN_BORDER = 1;
    private static final int ITEM_TEXTURE_SIZE = 16; // should never EVER change...
    private static final int BACKGROUND_TEXTURE_SIZE = 30;
    private static final int ITEM_OFFSET = OFFSET_FROM_SCREEN_BORDER + (BACKGROUND_TEXTURE_SIZE - ITEM_TEXTURE_SIZE) / 2;
    private static final Identifier EFFECT_BACKGROUND_TEXTURE = Mubble.id("hud/power_up_background");

    public static void renderPowerUpLayer(MinecraftClient client, DrawContext context, RenderTickCounter tickCounter) {
        var powerUpOpt = Optional.ofNullable(client.player).flatMap(PowerUpHolder::getPowerUp);
        if (powerUpOpt.isPresent()) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, EFFECT_BACKGROUND_TEXTURE, OFFSET_FROM_SCREEN_BORDER, OFFSET_FROM_SCREEN_BORDER, BACKGROUND_TEXTURE_SIZE, BACKGROUND_TEXTURE_SIZE);
            Sprite sprite = PowerUpSpriteManager.INSTANCE.getSprite(powerUpOpt.get());
            context.drawSpriteStretched(RenderLayer::getGuiTextured, sprite, ITEM_OFFSET, ITEM_OFFSET, ITEM_TEXTURE_SIZE, ITEM_TEXTURE_SIZE, ColorHelper.getWhite(1.0f));
        }
    }
}
