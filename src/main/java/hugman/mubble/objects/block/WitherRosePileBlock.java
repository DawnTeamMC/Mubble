package hugman.mubble.objects.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WitherRosePileBlock extends PileBlock
{
    public WitherRosePileBlock(Properties builder)
    {
        super(builder);
    }

    @Override
    public boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	Block block = state.getBlock();
    	return super.isValidGround(state, worldIn, pos) || block == Blocks.SOUL_SAND;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	for(int i = 0; i < 5; ++i)
    	{
    		if(rand.nextBoolean())
    		{
    			worldIn.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + (double)(rand.nextInt(17) / 16), (double)pos.getY() + (0.5D - (double)rand.nextFloat()), (double)pos.getZ() + (double)(rand.nextInt(17)  / 16), 0.0D, 0.0D, 0.0D);
    		}
    	}
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	if (!worldIn.isRemote && worldIn.getDifficulty() != Difficulty.PEACEFUL)
    	{
    		if (entityIn instanceof LivingEntity)
    		{
    			LivingEntity livingentity = (LivingEntity)entityIn;
    			if (!livingentity.isInvulnerableTo(DamageSource.WITHER))
    			{
    				livingentity.addPotionEffect(new EffectInstance(Effects.WITHER, 40));
    			}
    		}
    	}
    }
}
