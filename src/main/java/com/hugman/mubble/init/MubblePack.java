package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.Creator;
import com.hugman.dawn.api.creator.pack.Pack;
import com.hugman.mubble.Mubble;

public abstract class MubblePack extends Pack {
	protected static <V, B extends Creator.Builder<V>> V register(B creatorBuilder) {
		return add(creatorBuilder, Mubble.MOD_DATA);
	}

	protected static <P extends Pack, B extends Pack.Builder> P register(B packBuilder) {
		return add(packBuilder, Mubble.MOD_DATA);
	}
}
