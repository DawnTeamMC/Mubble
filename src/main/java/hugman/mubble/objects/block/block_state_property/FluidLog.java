package hugman.mubble.objects.block.block_state_property;

import net.minecraft.util.StringIdentifiable;

public enum FluidLog implements StringIdentifiable {
	EMPTY("empty"),
	WATER("water"),
	LAVA("lava");

	private final String name;

	FluidLog(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public String asString() {
		return this.name;
	}
}
