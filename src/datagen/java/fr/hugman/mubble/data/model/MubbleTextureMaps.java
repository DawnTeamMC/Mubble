package fr.hugman.mubble.data.model;

import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.util.Identifier;

public class MubbleTextureMaps {
    public static TextureMap all(Identifier all) {
        return new TextureMap().put(TextureKey.ALL, all.withPrefixedPath("block/"));
    }

    public static TextureMap sideEnd(Identifier side, Identifier end) {
        return new TextureMap()
                .put(TextureKey.SIDE, side.withPrefixedPath("block/"))
                .put(TextureKey.END, end.withPrefixedPath("block/"));
    }
}
