package hugman.mod.objects.blocks;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel
{
	public BlockBase(String name, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.MUBBLE_BLOCKS);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
		this.lightValue = light;
	}

	public BlockBase(String name, Material material, float hardness, float resistance, SoundType sound)
	{
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.MUBBLE_BLOCKS);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
	}
	
	public BlockBase(Material material, float hardness, float resistance, SoundType sound)
	{
		super(material);
		setCreativeTab(Main.MUBBLE_BLOCKS);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
	}
	
	public BlockBase(Material material, float hardness, float resistance)
	{
		super(material);
		setCreativeTab(Main.MUBBLE_BLOCKS);
		setHardness(hardness);
		this.blockResistance = resistance;
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
