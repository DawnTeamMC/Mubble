package hugman.mubble.objects.block.block_state_property;

import net.minecraft.util.IStringSerializable;

public enum Princess implements IStringSerializable
{
	NONE("none"),
	PEACH("peach"),
	DAISY("daisy");

	private final String name;

	private Princess(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return this.name;
	}

	public String getName()
	{
		return this.name;
	}
}
