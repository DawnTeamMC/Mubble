package fr.hugman.mubble.data.model;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;

public class MubbleTextureMaps {
    public static TextureMapping all(Identifier all) {
        return new TextureMapping().put(TextureSlot.ALL, all.withPrefix("block/"));
    }

    public static TextureMapping sideEnd(Identifier side, Identifier end) {
        return new TextureMapping()
                .put(TextureSlot.SIDE, side.withPrefix("block/"))
                .put(TextureSlot.END, end.withPrefix("block/"));
    }
}
