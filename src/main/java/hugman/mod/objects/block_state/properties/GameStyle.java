package hugman.mod.objects.block_state.properties;

import net.minecraft.util.IStringSerializable;

public enum GameStyle implements IStringSerializable
{
	SMB("smb"),
	SMB3("smb3"),
	SMW("smw"),
	NSMBU("nsmbu");

	private final String name;

	private GameStyle(String name)
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
