package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PermafrostPortalBlock extends NetherPortalBlock
{
    public PermafrostPortalBlock()
    {
        super(Properties.create(Material.PORTAL).doesNotBlockMovement().tickRandomly().hardnessAndResistance(-1.0F).sound(SoundType.GLASS).lightValue(11));
    }
    
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
    {
        if(worldIn.dimension.isSurfaceWorld() && worldIn.getGameRules().func_223586_b(GameRules.field_223601_d) && random.nextInt(2000) < worldIn.getDifficulty().getId())
        {
            while(worldIn.getBlockState(pos).getBlock() == this)
            {
                pos = pos.down();
            }
            if(worldIn.getBlockState(pos).canEntitySpawn(worldIn, pos, EntityType.ZOMBIE_PIGMAN))
            {
                Entity entity = MubbleEntities.ZOMBIE_COWMAN.spawn(worldIn, (CompoundNBT)null, (ITextComponent)null, (PlayerEntity)null, pos.up(), SpawnReason.STRUCTURE, false, false);
                if (entity != null)
                {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                }
            }
        }
    }
    
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
    	Direction.Axis enumfacing$axis = facing.getAxis();
        Direction.Axis enumfacing$axis1 = stateIn.get(AXIS);
        boolean flag = enumfacing$axis1 != enumfacing$axis && enumfacing$axis.isHorizontal();
        return !flag && facingState.getBlock() != this && !(new PermafrostPortalBlock.Size(worldIn, currentPos, enumfacing$axis1)).func_208508_f() ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
    
	public static class Size
	{
		private final IWorld world;
        private final Direction.Axis axis;
        private final Direction rightDir;
        private final Direction leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public Size(IWorld p_i48740_1_, BlockPos p_i48740_2_, Direction.Axis p_i48740_3_)
        {
        	this.world = p_i48740_1_;
        	this.axis = p_i48740_3_;
        	if (p_i48740_3_ == Direction.Axis.X)
        	{
        		this.leftDir = Direction.EAST;
        		this.rightDir = Direction.WEST;
        	}
        	else
        	{
        		this.leftDir = Direction.NORTH;
        		this.rightDir = Direction.SOUTH;
        	}
        	for(BlockPos blockpos = p_i48740_2_; p_i48740_2_.getY() > blockpos.getY() - 21 && p_i48740_2_.getY() > 0 && this.func_196900_a(p_i48740_1_.getBlockState(p_i48740_2_.down())); p_i48740_2_ = p_i48740_2_.down())
        	{
        		;
        	}

        	int i = this.getDistanceUntilEdge(p_i48740_2_, this.leftDir) - 1;
        	if (i >= 0)
        	{
        		this.bottomLeft = p_i48740_2_.offset(this.leftDir, i);
        		this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
        		if (this.width < 2 || this.width > 21)
        		{
        			this.bottomLeft = null;
        			this.width = 0;
        		}
        	}
        	if (this.bottomLeft != null)
        	{
        		this.height = this.calculatePortalHeight();
        	}
        }
        
        protected int getDistanceUntilEdge(BlockPos p_180120_1_, Direction p_180120_2_)
        {
        	int i;
        	for(i = 0; i < 22; ++i)
        	{
        		BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);
        		if (!this.func_196900_a(this.world.getBlockState(blockpos)) || this.world.getBlockState(blockpos.down()).getBlock() != MubbleBlocks.FROZEN_OBSIDIAN) break;
        	}
        	Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
        	return block == MubbleBlocks.FROZEN_OBSIDIAN ? i : 0;
        }

        public int getHeight()
        {
           return this.height;
        }

        public int getWidth()
        {
           return this.width;
        }

        protected int calculatePortalHeight()
        {
        	label56:
        		for(this.height = 0; this.height < 21; ++this.height)
        		{
        			for(int i = 0; i < this.width; ++i)
        			{
        				BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
        				BlockState iblockstate = this.world.getBlockState(blockpos);
        				if (!this.func_196900_a(iblockstate)) break label56;
        				
        				Block block = iblockstate.getBlock();
        				if (block == MubbleBlocks.PERMAFROST_PORTAL) ++this.portalBlockCount;
        				if (i == 0)
        				{
        					block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();
        					if (block != MubbleBlocks.FROZEN_OBSIDIAN) break label56;
        				}
        				else if (i == this.width - 1)
        				{
        					block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();
        					if (block != MubbleBlocks.FROZEN_OBSIDIAN) break label56;
        				}
        			}
        		}
        	for(int j = 0; j < this.width; ++j)
        	{
        		if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() != MubbleBlocks.FROZEN_OBSIDIAN)
        		{
        			this.height = 0;
        			break;
        		}
        	}
        	if (this.height <= 21 && this.height >= 3) return this.height;
        	else
        	{
        		this.bottomLeft = null;
        		this.width = 0;
        		this.height = 0;
        		return 0;
        	}
        }

        protected boolean func_196900_a(BlockState stateIn)
        {
        	Block block = stateIn.getBlock();
        	return stateIn.isAir() || block == Blocks.FIRE || block == MubbleBlocks.PERMAFROST_PORTAL;
        }

        public boolean isValid()
        {
        	return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void placePortalBlocks()
        {
        	for(int i = 0; i < this.width; ++i)
        	{
        		BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);
        		for(int j = 0; j < this.height; ++j) this.world.setBlockState(blockpos.up(j), MubbleBlocks.PERMAFROST_PORTAL.getDefaultState().with(PermafrostPortalBlock.AXIS, this.axis), 18);
        	}
        }

        private boolean func_196899_f()
        {
           return this.portalBlockCount >= this.width * this.height;
        }

        public boolean func_208508_f()
        {
           return this.isValid() && this.func_196899_f();
        }
     }
}