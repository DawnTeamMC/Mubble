package hugman.mod.objects.block.state.properties;

import net.minecraft.util.IStringSerializable;

public enum FluidLog implements IStringSerializable
{
	EMPTY("empty"),
	WATER("water"),
	LAVA("lava");

	private final String name;

	private FluidLog(String name)
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
