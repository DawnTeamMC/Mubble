package fr.hugman.mubble.platformer.network;

import fr.hugman.mubble.platformer.PlatformerVisual;
import fr.hugman.mubble.network.syncher.MubbleEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataSerializer;

public class PlatformerEntityDataSerializers {
    public static final EntityDataSerializer<PlatformerVisual> PLATFORMER_VISUAL = MubbleEntityDataSerializers.register("platformer_visual", PlatformerVisual.STREAM_CODEC);
}
