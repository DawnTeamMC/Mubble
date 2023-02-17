package fr.hugman.mubble.block;

import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

public enum BumpableDropMode implements StringIdentifiable {
	ALL(0, "all"),
	ONE(1, "one"),
	FORCED_ALL(2, "all");

	public static final Codec<BumpableDropMode> CODEC = StringIdentifiable.createCodec(BumpableDropMode::values);
	private final int index;
	private final String id;
	private final Text name;
	private final Text description;

	BumpableDropMode(int index, String id) {
		this.index = index;
		this.id = id;
		this.name = Text.translatable("bumpable.drop." + id);
		this.description = Text.translatable("bumpable.drop." + id + ".description");
	}

	public static BumpableDropMode get(int index) {
		for(BumpableDropMode mode : values()) {
			if(mode.index == index) {
				return mode;
			}
		}
		return ALL;
	}

	public static BumpableDropMode get(String s) {
		for(BumpableDropMode mode : values()) {
			if(mode.id.equals(s)) {
				return mode;
			}
		}
		return ALL;
	}

	public BumpableDropMode next() {
		if(this == FORCED_ALL) {
			return this;
		}
		BumpableDropMode dropMode = get((index + 1) % values().length);
		if(dropMode == FORCED_ALL) {
			return get((index + 1) % values().length);
		}
		return dropMode;
	}

	public int getIndex() {
		return index;
	}

	public Text getName() {
		return name;
	}

	public Text getDescription() {
		return description;
	}

	@Override
	public String asString() {
		return this.id;
	}
}
