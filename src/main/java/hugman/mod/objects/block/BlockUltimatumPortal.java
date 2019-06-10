package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockUltimatumPortal extends Block
{	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 6D, 0.0D, 16.0D, 10.0D, 16.0D);
	
    public BlockUltimatumPortal()
    {
        super(Properties.from(Blocks.NETHER_PORTAL).hardnessAndResistance(-1.0F, 3600000.0F));
    }
    
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return SHAPE;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) 
    {
    	return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
    	return false;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
    	return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return 0;
    }
    
    @Override
    public int quantityDropped(IBlockState state, Random random)
    {
    	return 0;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	if (rand.nextInt(100) == 0)
        {
            worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, MubbleSounds.BLOCK_ULTIMATUM_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        }
        if (rand.nextInt(30) == 0)
        {
        	BasicParticleType particle = null;
            switch (rand.nextInt(4))
            {
            	case 0: particle = Particles.FLAME;
    					break;
            	case 1: particle = Particles.SMOKE;
						break;
            	case 2: particle = Particles.CLOUD;
						break;
            	case 3: particle = Particles.FIREWORK;
						break;
            }
            for (int i = 0; i < rand.nextInt(11) + 5; i++)
            {
            	worldIn.spawnParticle(particle, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((double)rand.nextFloat() - 0.5D) * 0.3D, (double)rand.nextFloat() * 0.9D, ((double)rand.nextFloat() - 0.5D) * 0.3D);
            }
        }
    }
    
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if (!worldIn.isRemote && !entityIn.isPassenger() && !entityIn.isBeingRidden() && entityIn.isNonBoss() && VoxelShapes.compare(VoxelShapes.create(entityIn.getBoundingBox().offset((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()))), state.getShape(worldIn, pos), IBooleanFunction.AND))
        {
        	/*if(worldIn.getDimension() == MubbleDimensions.ULTIMATUM.getFactory()) entityIn.changeDimension(MubbleDimensions.ULTIMATUM_TYPE, new Teleporter(entityIn.getServer().getWorld(DimensionType.OVERWORLD)));
        	else entityIn.changeDimension(MubbleDimensions.ULTIMATUM_TYPE, new Teleporter(entityIn.getServer().getWorld(MubbleDimensions.ULTIMATUM_TYPE)));*/
        }
    }
}
