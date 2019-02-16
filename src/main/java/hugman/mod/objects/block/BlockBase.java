package hugman.mod.objects.block;

import hugman.mod.Main;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel
{   
	/** 
	 * Open class - can be initialized for multiple blocks with variables.<br>
	 * Template class - is used to create other classes.
	 */
	public BlockBase(String name, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(material);
		setCreativeTab(MubbleTabs.MUBBLE_BLOCKS);
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
		this.lightValue = light;
		
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	/** 
	 * Open class - can be initialized for multiple items with variables.<br>
	 * Template class - is used to create other classes.
	 */
	public BlockBase(String name, Material material, float hardness, float resistance, SoundType sound)
	{
		super(material);
		setCreativeTab(MubbleTabs.MUBBLE_BLOCKS);
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
		
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	/** 
	 * Open class - can be initialized for multiple items with variables.<br>
	 * Template class - is used to create other classes.
	 */
	public BlockBase(Material material, float hardness, float resistance, SoundType sound)
	{
		super(material);
		setCreativeTab(MubbleTabs.MUBBLE_BLOCKS);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
		
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	/** 
	 * Open class - can be initialized for multiple items with variables.<br>
	 * Template class - is used to create other classes.
	 */
	public BlockBase(Material material, float hardness, float resistance)
	{
		super(material);
		setCreativeTab(MubbleTabs.MUBBLE_BLOCKS);
		setHardness(hardness);
		this.blockResistance = resistance;
		
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
