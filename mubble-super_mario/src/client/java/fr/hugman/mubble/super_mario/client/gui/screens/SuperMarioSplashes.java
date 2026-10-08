package fr.hugman.mubble.super_mario.client.gui.screens;

import fr.hugman.mubble.client.gui.screens.MubbleSplashes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class SuperMarioSplashes {
    private SuperMarioSplashes() {}

    public static void register() {
        MubbleSplashes.addAll(
                "L is real 2401",
                "H is real 3107", // Herobrine variant
                "Let's-a go!",
                "It's-a me, Steve!",
                "So long-a, Warden!",
                "Smash through the blocks that bar your way!",
                "It's time to jump up in the air!"
        );
    }
}
