package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;

public class BlockButtonStone extends net.minecraft.block.BlockButtonStone
{
    public BlockButtonStone(String name, Block base_block)
    {
        super(Properties.create(Material.CIRCUITS).doesNotBlockMovement().hardnessAndResistance(0.5F));
        setRegistryName(Mubble.MOD_ID, name + "_button");
        MubbleBlocks.register(this, ItemGroup.REDSTONE);
    }
    
    public BlockButtonStone(Block base_block)
    {
        super(Properties.create(Material.CIRCUITS).doesNotBlockMovement().hardnessAndResistance(0.5F));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_button");
        MubbleBlocks.register(this, ItemGroup.REDSTONE);
    }
}