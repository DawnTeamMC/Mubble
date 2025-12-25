package fr.hugman.mubble.client.gui.hud;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.power_up.action.ShootProjectilePowerUpAction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;

@Environment(EnvType.CLIENT)
public class PowerUpPropertiesHudRendering {
    private static final int PROJECTILE_WIDTH = 4;
    private static final int PROJECTILE_HEIGHT = 4;
    private static final int PROJECTILE_PADDING = 1;
    private static final int MARGIN_FROM_CROSSHAIR = 5;
    private static final Identifier PROJECTILE_TEXTURE = Mubble.id("hud/power_up_projectile");
    private static final Identifier PROJECTILE_EMPTY_TEXTURE = Mubble.id("hud/power_up_projectile_empty");
    private static final int MAX_PROJECTILES = 6;

    public static void renderProjectilesLayer(Minecraft client, GuiGraphics context) {
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
        int projectilesMax = getProjectilesMax(client.player);
        if (projectilesMax > 0) {
            var properties = client.player.getPowerUpProperties();
            if(properties == null) {
                //TODO: log a warning?
                return;
            }
            var projectileCount = projectilesMax - properties.getProjectiles().size();
            if(projectileCount > MAX_PROJECTILES) {
                return;
            }
            int startX = (context.guiWidth() + 8) / 2 + MARGIN_FROM_CROSSHAIR;
            int y = (context.guiHeight() - PROJECTILE_HEIGHT) / 2;
            for (int i = Math.min(MAX_PROJECTILES, projectilesMax) - 1; i >= 0; i--) {
                int x = startX + (i * (PROJECTILE_WIDTH + PROJECTILE_PADDING));
                context.blitSprite(RenderPipelines.CROSSHAIR, i < projectileCount ? PROJECTILE_TEXTURE : PROJECTILE_EMPTY_TEXTURE, x, y, PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
            }
        }
    }

    private static int getProjectilesMax(LocalPlayer player) {
        var powerUp = player.getPowerUp();
        if (powerUp.isEmpty()) {
            return 0;
        }
        var action = powerUp.get().value().action();
        if (action.isEmpty()) {
            return 0;
        }
        if (!(action.get().value() instanceof ShootProjectilePowerUpAction shootProjectilePowerUpAction)) {
            return 0;
        }
        return shootProjectilePowerUpAction.maxProjectiles().orElse(0);
    }
}
