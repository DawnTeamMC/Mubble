package hugman.mubble.util.creator;

public enum BlockShape {
	CUBE(""),
	STAIRS("stairs"),
	SLAB("slab"),
	VERTICAL_SLAB("vertical_slab"),
	WALL("wall"),
	BUTTON("button"),
	PRESSURE_PLATE("pressure_plate");

	private final String suffix;

	BlockShape(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		if(suffix.isEmpty()) {
			return suffix;
		}
		else {
			return "_" + suffix;
		}
	}
}
