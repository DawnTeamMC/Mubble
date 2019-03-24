package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockFlower extends net.minecraft.block.BlockFlower
{
    public BlockFlower(String name)
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT));
        setRegistryName(Mubble.MOD_ID, name);
        MubbleBlocks.register(this);
        MubbleBlocks.registerWithoutItem(new BlockFlowerPot("potted_" + name, this));
    }
    
    public BlockFlower(String name, int light)
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(light));
        setRegistryName(Mubble.MOD_ID, name);
        MubbleBlocks.register(this);
        MubbleBlocks.registerWithoutItem(new BlockFlowerPot("potted_" + name, this));
    }
}
