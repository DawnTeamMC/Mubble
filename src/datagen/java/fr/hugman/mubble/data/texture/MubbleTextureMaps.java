package fr.hugman.mubble.data.texture;

import net.minecraft.block.Block;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.util.Identifier;

public class MubbleTextureMaps {
	public static TextureMap palettedSideEnd(Identifier id, String suffix) {
		return new TextureMap()
				.put(TextureKey.SIDE, id.withPath(path -> "block/" + path + "/side_" + suffix))
				.put(TextureKey.END, id.withPath(path -> "block/" + path + "/end_" + suffix));
	}
}
