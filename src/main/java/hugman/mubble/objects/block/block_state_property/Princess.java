package hugman.mubble.objects.block.block_state_property;

import net.minecraft.util.StringIdentifiable;

public enum Princess implements StringIdentifiable
{
	NONE("none"),
	PEACH("peach"),
	DAISY("daisy");

	private final String name;

	private Princess(String name)
	{
		this.name = name;
	}

	public String asString()
	{
		return this.name;
	}

	public String getName()
	{
		return this.name;
	}
}
