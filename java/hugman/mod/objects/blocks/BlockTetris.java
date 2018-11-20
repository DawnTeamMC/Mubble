package hugman.mod.objects.blocks;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockTetris extends BlockFalling implements IHasModel
{	
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockTetris(String color, String type, Material material)
	{
		super(material);
		setTranslationKey(color+"_tetris_"+type);
		setRegistryName(color+"_tetris_"+type);
		setCreativeTab(Main.MUBBLE_BLOCKS);
		if(material==Material.ROCK)
		{
			setHardness(1.5f);
			this.blockResistance = 30f;
			setSoundType(SoundType.STONE);
		}
		if(material==Material.CLOTH)
		{
			setHardness(0.8f);
			this.blockResistance = 4f;
			setSoundType(SoundType.CLOTH);
		}
		if(material==Material.IRON)
		{
			setHardness(5f);
			this.blockResistance = 30f;
			setSoundType(SoundType.METAL);
		}
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
