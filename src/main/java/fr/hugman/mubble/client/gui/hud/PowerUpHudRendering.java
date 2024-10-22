package fr.hugman.mubble.client.gui.hud;

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
    private static final Identifier EFFECT_BACKGROUND_TEXTURE = Identifier.ofVanilla("hud/effect_background");

    public static void renderPowerUpLayer(MinecraftClient client, DrawContext context, RenderTickCounter tickCounter) {
        var powerUpOpt = Optional.ofNullable(client.player).flatMap(PowerUpHolder::getPowerUp);
        if (powerUpOpt.isPresent()) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, EFFECT_BACKGROUND_TEXTURE, 1, 1, 24, 24);
            Sprite sprite = PowerUpSpriteManager.INSTANCE.getSprite(powerUpOpt.get());
            context.drawSprite(RenderLayer::getGuiTextured, sprite, 1 + 4, 1 + 4, 16, 16, ColorHelper.getWhite(1.0f));
        }
    }
}
