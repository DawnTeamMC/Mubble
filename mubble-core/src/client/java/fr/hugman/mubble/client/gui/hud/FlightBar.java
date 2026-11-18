package fr.hugman.mubble.client.gui.hud;

import fr.hugman.mubble.world.power_up.ability.FlightAbility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBar;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * The flight gauge, drawn in the place of the jump bar of a mount.
 */
@Environment(EnvType.CLIENT)
public class FlightBar implements ContextualBar {
    private static final Identifier DEFAULT_BACKGROUND = Identifier.withDefaultNamespace("hud/jump_bar_background");
    private static final Identifier DEFAULT_PROGRESS = Identifier.withDefaultNamespace("hud/jump_bar_progress");
    private static final int FLASH_TICKS = 6;
    private static final float LOW_SHARE = 0.25F;
    private static final int BLINK_TICKS = 3;
    private static final float EXHAUSTED_SHADE = 0.45F;

    private static FlightBar instance;

    private final Minecraft minecraft;
    private int lastGauge = -1;
    private int flashStart = Integer.MIN_VALUE;

    private FlightBar(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public static boolean shouldShow(Player player) {
        return player.getFlightAbility().map(flight -> player.isFlying() || player.getFlightGauge() < flight.gauge()).orElse(false);
    }

    public static FlightBar get(Minecraft minecraft) {
        if (instance == null) {
            instance = new FlightBar(minecraft);
        }
        return instance;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Player player = this.minecraft.player;
        if (player == null || player.getFlightAbility().isEmpty()) {
            return;
        }
        FlightAbility flight = player.getFlightAbility().get();
        int gauge = player.getFlightGauge();
        float ticks = player.tickCount + deltaTracker.getGameTimeDeltaPartialTick(false);
        if (this.lastGauge >= 0 && gauge > this.lastGauge) {
            this.flashStart = player.tickCount;
        }
        this.lastGauge = gauge;

        int left = this.left(this.minecraft.getWindow());
        int top = this.top(this.minecraft.getWindow());
        Identifier background = flight.bar().map(FlightAbility.Bar::background).orElse(DEFAULT_BACKGROUND);
        Identifier progress = flight.bar().map(FlightAbility.Bar::progress).orElse(DEFAULT_PROGRESS);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, left, top, WIDTH, HEIGHT);

        float share = gauge / (float) flight.gauge();
        int filled = Mth.lerpDiscrete(share, 0, WIDTH);
        if (filled <= 0) {
            return;
        }
        float flash = 1.0F - Mth.clamp((ticks - this.flashStart) / FLASH_TICKS, 0.0F, 1.0F);
        boolean blinkOff = flash <= 0.0F && share < LOW_SHARE && player.isFlying() && ((int) ticks / BLINK_TICKS) % 2 == 0;
        float shade = gauge < flight.restart() && !player.isFlying() ? EXHAUSTED_SHADE : 1.0F;
        int color = blinkOff ? ARGB.colorFromFloat(0.35F, 1.0F, 1.0F, 1.0F) : ARGB.colorFromFloat(1.0F, shade, shade, shade);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, progress, WIDTH, HEIGHT, 0, 0, left, top, filled, HEIGHT, color);
        if (flash > 0.0F) {
            graphics.fill(left, top, left + filled, top + HEIGHT, ARGB.colorFromFloat(flash * 0.7F, 1.0F, 1.0F, 1.0F));
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
    }
}
