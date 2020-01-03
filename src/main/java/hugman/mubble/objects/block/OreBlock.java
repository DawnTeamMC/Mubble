package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class OreBlock extends net.minecraft.block.OreBlock
{    
    public OreBlock(Settings properties)
    {
        super(properties);
    }
    
    @Override
    protected int getExperienceWhenMined(Random rand)
    {
    	if (this == MubbleBlocks.VANADIUM_ORE) return MathHelper.nextInt(rand, 4, 8);
    	return super.getExperienceWhenMined(rand);
    }
    
    @Override
    public void onStacksDropped(BlockState state, World world, BlockPos pos, ItemStack stack)
    {
    	if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0)
    	{
    		this.getExperienceWhenMined(world.random);
    	}
    }
}
