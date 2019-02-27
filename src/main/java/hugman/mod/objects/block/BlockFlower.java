package hugman.mod.objects.block;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockFlower extends net.minecraft.block.BlockFlower
{
    public BlockFlower(String name)
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT));
        setRegistryName(Reference.MOD_ID, name);
        MubbleBlocks.register(this);
    }
    
    public BlockFlower(String name, int light)
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(light));
        setRegistryName(Reference.MOD_ID, name);
        MubbleBlocks.register(this);
    }
}
