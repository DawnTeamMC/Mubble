package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;

public class BlockCake extends net.minecraft.block.BlockCake
{
    public BlockCake(String name)
    {
        super(Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH));
        setRegistryName(Mubble.MOD_ID, name);
        MubbleBlocks.register(this, ItemGroup.FOOD);
    }
}