package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BlockPermafrostPortal extends net.minecraft.block.BlockPortal
{
    public BlockPermafrostPortal(String name)
    {
        super(Properties.create(Material.PORTAL).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(-1.0F).sound(SoundType.GLASS).lightValue(11));
        setRegistryName(Mubble.MOD_ID, "permafrost_portal");
        MubbleBlocks.registerWithoutItem(this);
    }
    
	public static class Size
	{
		private final IWorld world;
        private final EnumFacing.Axis axis;
        private final EnumFacing rightDir;
        private final EnumFacing leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public Size(IWorld p_i48740_1_, BlockPos p_i48740_2_, EnumFacing.Axis p_i48740_3_)
        {
        	this.world = p_i48740_1_;
        	this.axis = p_i48740_3_;
        	if (p_i48740_3_ == EnumFacing.Axis.X)
        	{
        		this.leftDir = EnumFacing.EAST;
        		this.rightDir = EnumFacing.WEST;
        	}
        	else
        	{
        		this.leftDir = EnumFacing.NORTH;
        		this.rightDir = EnumFacing.SOUTH;
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
        
        protected int getDistanceUntilEdge(BlockPos p_180120_1_, EnumFacing p_180120_2_)
        {
        	int i;
        	for(i = 0; i < 22; ++i)
        	{
        		BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);
        		if (!this.func_196900_a(this.world.getBlockState(blockpos)) || this.world.getBlockState(blockpos.down()).getBlock() != Blocks.OBSIDIAN) break;
        	}
        	Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
        	return block == Blocks.OBSIDIAN ? i : 0;
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
        				IBlockState iblockstate = this.world.getBlockState(blockpos);
        				if (!this.func_196900_a(iblockstate)) break label56;
        				
        				Block block = iblockstate.getBlock();
        				if (block == Blocks.NETHER_PORTAL) ++this.portalBlockCount;
        				if (i == 0)
        				{
        					block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();
        					if (block != Blocks.OBSIDIAN) break label56;
        				}
        				else if (i == this.width - 1)
        				{
        					block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();
        					if (block != Blocks.OBSIDIAN) break label56;
        				}
        			}
        		}
        	for(int j = 0; j < this.width; ++j)
        	{
        		if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() != Blocks.OBSIDIAN)
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

        protected boolean func_196900_a(IBlockState stateIn)
        {
        	Block block = stateIn.getBlock();
        	return stateIn.isAir() || block == Blocks.FIRE || block == Blocks.NETHER_PORTAL;
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
        		for(int j = 0; j < this.height; ++j) this.world.setBlockState(blockpos.up(j), Blocks.NETHER_PORTAL.getDefaultState().with(BlockPermafrostPortal.AXIS, this.axis), 18);
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