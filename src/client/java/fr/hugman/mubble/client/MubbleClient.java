package fr.hugman.mubble.client;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.client.gui.screen.BumpableScreen;
import fr.hugman.mubble.client.keybind.MubbleKeyBindings;
import fr.hugman.mubble.client.render.MubbleRenderers;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.client.network.MubbleClientReceivers;
import fr.hugman.mubble.entity.KoopaShellEntity;
import fr.hugman.mubble.screen.MubbleScreenHandlerTypes;
import fr.hugman.mubble.sound.MubbleSounds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(MubbleModelLayers.class);

        registerBlockRenderLayers();
        registerHandledScreens();
        MubbleRenderers.registerEntities();
        MubbleRenderers.registerBlockEntities();
        MubbleKeyBindings.registerEvents();
        MubbleClientReceivers.register();
    }

    private static void registerBlockRenderLayers() {
        BlockRenderLayerMap.putBlock(MubbleBlocks.RED_BEEP_BLOCK, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(MubbleBlocks.BLUE_BEEP_BLOCK, BlockRenderLayer.CUTOUT);
    }

    private static void registerHandledScreens() {
        HandledScreens.register(MubbleScreenHandlerTypes.BUMPABLE_BLOCK, BumpableScreen::new);
    }

    @Environment(EnvType.CLIENT)
    public static class MovingKoopaShellSoundInstance extends MovingSoundInstance {
        private final KoopaShellEntity shell;
        private float distance = 0.0F;

        public MovingKoopaShellSoundInstance(KoopaShellEntity shell) {
            super(MubbleSounds.KOOPA_SHELL_SLIDE, SoundCategory.NEUTRAL, SoundInstance.createRandom());
            this.shell = shell;
            this.repeat = true;
            this.repeatDelay = 0;
            this.volume = 0.3F;
        }


        @Override
        public boolean canPlay() {
            return !this.shell.isSilent();
        }

        @Override
        public boolean shouldAlwaysPlay() {
            return true;
        }

        @Override
        public void tick() {
            if (this.shell.isRemoved()) {
                this.setDone();
            } else {
                this.x = ((float) this.shell.getX());
                this.y = ((float) this.shell.getY());
                this.z = ((float) this.shell.getZ());
                float f = (float) this.shell.getVelocity().horizontalLength();
                if (f >= 0.01F && this.shell.getWorld().getTickManager().shouldTick()) {
                    this.distance = MathHelper.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
                    this.volume = MathHelper.lerp(MathHelper.clamp(f, 0.0F, 0.5F), 0.0F, 0.7F);
                } else {
                    this.distance = 0.0F;
                    this.volume = 0.0F;
                }
            }
        }
    }
}
