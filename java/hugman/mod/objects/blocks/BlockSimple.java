package hugman.mod.objects.blocks;

import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockSimple extends BlockBase implements IHasModel
{
	public BlockSimple(String name, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(name, material, hardness, resistance, sound, light);

		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	public BlockSimple(String name, Material material, float hardness, float resistance, SoundType sound)
	{
		super(name, material, hardness, resistance, sound);

		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
}
