package fr.hugman.mubble.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    /**
     * Ticks a snapshot of the level list instead of the live map.
     *
     * <p>{@code getAllLevels()} hands out the values view of the server's level map, and
     * {@code tickChildren} iterates it directly. Voyages add and remove levels in response to
     * player actions, which can land inside that iteration and blow up with a
     * {@link java.util.ConcurrentModificationException}. Copying first costs one small array per
     * tick and makes the timing of a level opening or closing irrelevant.
     *
     * <p>A level added mid-tick simply starts ticking next tick.
     *
     * <p>This deliberately has no {@code require = 0}: if a future Minecraft version stops calling
     * {@code getAllLevels()} here, mixin should fail at load rather than let the hazard back in
     * silently.
     */
    @Redirect(
            method = "tickChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getAllLevels()Ljava/lang/Iterable;")
    )
    private Iterable<ServerLevel> mubble$snapshotLevelsBeforeTicking(MinecraftServer server) {
        return ImmutableList.copyOf(server.getAllLevels());
    }
}
