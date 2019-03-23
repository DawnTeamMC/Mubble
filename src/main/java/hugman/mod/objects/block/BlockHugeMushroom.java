package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;

public class BlockHugeMushroom extends net.minecraft.block.BlockHugeMushroom
{
    public BlockHugeMushroom(Block mushroom, EnumDyeColor color)
    {
        super(mushroom, Block.Properties.create(Material.WOOD, color).hardnessAndResistance(0.2F).sound(SoundType.WOOD));
        setRegistryName(Mubble.MOD_ID, color.getTranslationKey() + "_mushroom_block");
        MubbleBlocks.register(this);
    }
}
