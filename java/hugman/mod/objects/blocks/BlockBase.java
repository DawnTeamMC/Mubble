package hugman.mod.objects.blocks;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.CreativeTabInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel
{   
	/** 
	 * Open class - can be initialized for multiple blocks with variables.<br>
	 * Template class - is used to create other classes.
	 */
	public BlockBase(String name, CreativeTabs tab, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(tab);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
		this.lightValue = light;
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	/** 
	 * Open class - can be initialized for multiple items with variables.<br>
	 * Template class - is used to create other classes.
	 */
	public BlockBase(String name, CreativeTabs tab, Material material, float hardness, float resistance, SoundType sound)
	{
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(tab);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	/** 
	 * Open class - can be initialized for multiple items with variables.<br>
	 * Template class - is used to create other classes.
	 */
	public BlockBase(CreativeTabs tab, Material material, float hardness, float resistance, SoundType sound)
	{
		super(material);
		setCreativeTab(tab);
		setHardness(hardness);
		this.blockResistance = resistance;
		setSoundType(sound);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	/** 
	 * Open class - can be initialized for multiple items with variables.<br>
	 * Template class - is used to create other classes.
	 */
	public BlockBase(CreativeTabs tab, Material material, float hardness, float resistance)
	{
		super(material);
		setCreativeTab(tab);
		setHardness(hardness);
		this.blockResistance = resistance;
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
