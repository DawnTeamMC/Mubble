package fr.hugman.mubble.arcade.network;

import fr.hugman.mubble.arcade.ArcadeVisual;
import fr.hugman.mubble.network.syncher.MubbleEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataSerializer;

public class ArcadeEntityDataSerializers {
    public static final EntityDataSerializer<ArcadeVisual> ARCADE_VISUAL = MubbleEntityDataSerializers.register("arcade_visual", ArcadeVisual.STREAM_CODEC);
}
