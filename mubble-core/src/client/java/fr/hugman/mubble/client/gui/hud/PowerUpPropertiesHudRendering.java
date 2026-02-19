package fr.hugman.mubble.client.gui.hud;

import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;

@Environment(EnvType.CLIENT)
public class PowerUpPropertiesHudRendering {
    private static final int CHARGE_WIDTH = 4;
    private static final int CHARGE_HEIGHT = 4;
    private static final int CHARGE_PADDING = 1;
    private static final int MARGIN_FROM_CROSSHAIR = 5;
    //TODO: change paths
    private static final Identifier CHARGE_TEXTURE = Mubble.id("hud/power_up_projectile");
    private static final Identifier CHARGE_EMPTY_TEXTURE = Mubble.id("hud/power_up_projectile_empty");
    private static final int MAX_CHARGE_DISPLAY = 6;

    public static void renderChargesLayer(Minecraft client, GuiGraphics context) {
        if (client.player == null) {
            //TODO: log a warning?
            return;
        }
        Options gameOptions = client.options;
        if (!gameOptions.getCameraType().isFirstPerson()) {
            return;
        }
        if (client.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            return;
        }
        if (client.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR)) {
            return;
        }
        var properties = client.player.getPowerUpProperties();
        if(properties == null) {
            //TODO: log a warning?
            return;
        }
        int chargesMax = properties.maxCharges;
        if (chargesMax > 0 && chargesMax != Integer.MAX_VALUE) {
            var chargeCount = properties.getChargeCount();
            if(chargeCount > MAX_CHARGE_DISPLAY) {
                return;
            }
            int startX = (context.guiWidth() + 8) / 2 + MARGIN_FROM_CROSSHAIR;
            int y = (context.guiHeight() - CHARGE_HEIGHT) / 2;
            for (int i = Math.min(MAX_CHARGE_DISPLAY, chargesMax) - 1; i >= 0; i--) {
                int x = startX + (i * (CHARGE_WIDTH + CHARGE_PADDING));
                context.blitSprite(RenderPipelines.CROSSHAIR, i < chargeCount ? CHARGE_TEXTURE : CHARGE_EMPTY_TEXTURE, x, y, CHARGE_WIDTH, CHARGE_HEIGHT);
            }
        }
    }
}
