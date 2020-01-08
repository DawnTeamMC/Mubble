package hugman.mubble.objects.block.block_state_property;

import net.minecraft.util.StringIdentifiable;

public enum VerticalSlabType implements StringIdentifiable
{
	NORTH("north"),
	SOUTH("south"),
	EAST("east"),
	WEST("west"),
	DOUBLE("double");

	private final String name;

	private VerticalSlabType(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return this.name;
	}

	public String asString()
	{
		return this.name;
	}
}
