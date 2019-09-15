package hugman.mubble.objects.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.cache.LoadingCache;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.util.teleporters.DimensionTeleporterManager;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PermafrostPortalBlock extends Block
{
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
	protected static final VoxelShape X_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
	protected static final VoxelShape Z_SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

	public PermafrostPortalBlock(Properties builder)
	{
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.X));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(AXIS);
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) 
	{
		switch ((Direction.Axis) state.get(AXIS))
		{
		case Z:
			return Z_SHAPE;
		case X:
		default:
			return X_SHAPE;
		}
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rot)
	{
		switch (rot)
		{
		case COUNTERCLOCKWISE_90:
		case CLOCKWISE_90:
			switch ((Direction.Axis) state.get(AXIS))
			{
			case Z:
				return state.with(AXIS, Direction.Axis.X);
			case X:
				return state.with(AXIS, Direction.Axis.Z);
			default:
				return state;
			}
		default:
			return state;
		}
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		if(worldIn.dimension.isSurfaceWorld() && worldIn.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && random.nextInt(2000) < worldIn.getDifficulty().getId())
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

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (rand.nextInt(100) == 0)
		{
			worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int i = 0; i < 4; ++i)
		{
			double d0 = (double) ((float) pos.getX() + rand.nextFloat());
			double d1 = (double) ((float) pos.getY() + rand.nextFloat());
			double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
			double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			int j = rand.nextInt(2) * 2 - 1;
			if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this)
			{
				d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
				d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
			} else
			{
				d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
				d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
			}

			worldIn.addParticle(ParticleTypes.TOTEM_OF_UNDYING, d0, d1, d2, d3, d4, d5);
		}
	}
	
	/* PORTAL STUFF */

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if(!entityIn.isPassenger() && !entityIn.isBeingRidden() && entityIn.isNonBoss())
		{
			DimensionTeleporterManager.teleport(worldIn, entityIn, MubbleDimensions.PERMAFROST);
		}
	}
	
	public boolean trySpawnPortal(IWorld worldIn, BlockPos pos) 
	{
		PermafrostPortalBlock.Size permafrostPortalBlock$size = this.isPortal(worldIn, pos);
		if (permafrostPortalBlock$size != null)
		{
			permafrostPortalBlock$size.placePortalBlocks();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Nullable
	public PermafrostPortalBlock.Size isPortal(IWorld worldIn, BlockPos pos)
	{
		PermafrostPortalBlock.Size permafrostPortalBlock$size = new PermafrostPortalBlock.Size(worldIn, pos, Direction.Axis.X);
		if (permafrostPortalBlock$size.isValid() && permafrostPortalBlock$size.portalBlockCount == 0)
		{
			return permafrostPortalBlock$size;
		}
		else
		{
			PermafrostPortalBlock.Size permafrostPortalBlock$size1 = new PermafrostPortalBlock.Size(worldIn, pos, Direction.Axis.Z);
			return permafrostPortalBlock$size1.isValid() && permafrostPortalBlock$size1.portalBlockCount == 0 ? permafrostPortalBlock$size1 : null;
		}
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		Direction.Axis direction$axis = facing.getAxis();
		Direction.Axis direction$axis1 = stateIn.get(AXIS);
		boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
		return !flag && facingState.getBlock() != this && !(new PermafrostPortalBlock.Size(worldIn, currentPos, direction$axis1)).isCorrectPortal() ? Blocks.AIR.getDefaultState() : stateIn;
	}

	public BlockPattern.PatternHelper createPatternHelper(IWorld worldIn, BlockPos pos)
	{
		Direction.Axis direction$axis = Direction.Axis.Z;
		PermafrostPortalBlock.Size permafrostPortalBlock$size = new PermafrostPortalBlock.Size(worldIn, pos, Direction.Axis.X);
		LoadingCache<BlockPos, CachedBlockInfo> loadingcache = BlockPattern.createLoadingCache(worldIn, true);
		if (!permafrostPortalBlock$size.isValid())
		{
			direction$axis = Direction.Axis.X;
			permafrostPortalBlock$size = new PermafrostPortalBlock.Size(worldIn, pos, Direction.Axis.Z);
		}

		if (!permafrostPortalBlock$size.isValid())
		{
			return new BlockPattern.PatternHelper(pos, Direction.NORTH, Direction.UP, loadingcache, 1, 1, 1);
		}
		else
		{
			int[] aint = new int[Direction.AxisDirection.values().length];
			Direction direction = permafrostPortalBlock$size.rightDir.rotateYCCW();
			BlockPos blockpos = permafrostPortalBlock$size.bottomLeft.up(permafrostPortalBlock$size.getHeight() - 1);

			for (Direction.AxisDirection direction$axisdirection : Direction.AxisDirection.values())
			{
				BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper(direction.getAxisDirection() == direction$axisdirection ? blockpos : blockpos.offset(permafrostPortalBlock$size.rightDir, permafrostPortalBlock$size.getWidth() - 1), Direction.getFacingFromAxis(direction$axisdirection, direction$axis), Direction.UP, loadingcache, permafrostPortalBlock$size.getWidth(), permafrostPortalBlock$size.getHeight(), 1);

				for (int i = 0; i < permafrostPortalBlock$size.getWidth(); ++i)
				{
					for (int j = 0; j < permafrostPortalBlock$size.getHeight(); ++j)
					{
						CachedBlockInfo cachedblockinfo = blockpattern$patternhelper.translateOffset(i, j, 1);
						if(!(cachedblockinfo.getBlockState().getBlock() instanceof AirBlock))
						{
							++aint[direction$axisdirection.ordinal()];
						}
					}
				}
			}

			Direction.AxisDirection direction$axisdirection1 = Direction.AxisDirection.POSITIVE;

			for (Direction.AxisDirection direction$axisdirection2 : Direction.AxisDirection.values())
			{
				if (aint[direction$axisdirection2.ordinal()] < aint[direction$axisdirection1.ordinal()])
				{
					direction$axisdirection1 = direction$axisdirection2;
				}
			}

			return new BlockPattern.PatternHelper(direction.getAxisDirection() == direction$axisdirection1 ? blockpos : blockpos.offset(permafrostPortalBlock$size.rightDir, permafrostPortalBlock$size.getWidth() - 1), Direction.getFacingFromAxis(direction$axisdirection1, direction$axis), Direction.UP, loadingcache, permafrostPortalBlock$size.getWidth(), permafrostPortalBlock$size.getHeight(), 1);
		}
	}
	
	public static class Size
	{
		private final IWorld world;
		private final Direction.Axis axis;
		private final Direction rightDir;
		private final Direction leftDir;
		private int portalBlockCount;
		@Nullable
		private BlockPos bottomLeft;
		private int height;
		private int width;

		public Size(IWorld worldIn, BlockPos pos, Direction.Axis direction)
		{
			this.world = worldIn;
			this.axis = direction;
			if (direction == Direction.Axis.X)
			{
				this.leftDir = Direction.EAST;
				this.rightDir = Direction.WEST;
			}
			else
			{
				this.leftDir = Direction.NORTH;
				this.rightDir = Direction.SOUTH;
			}

			for(BlockPos blockpos = pos; pos.getY() > blockpos.getY() - 21 && pos.getY() > 0 && this.isReplacable(worldIn.getBlockState(pos.down())); pos = pos.down()) {;}

			int i = this.getDistanceUntilEdge(pos, this.leftDir) - 1;
			if (i >= 0)
			{
				this.bottomLeft = pos.offset(this.leftDir, i);
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

		protected int getDistanceUntilEdge(BlockPos pos, Direction direction)
		{
			int i;
			for (i = 0; i < 22; ++i)
			{
				BlockPos blockpos = pos.offset(direction, i);
				if (!this.isReplacable(this.world.getBlockState(blockpos)) || this.world.getBlockState(blockpos.down()).getBlock() != MubbleBlocks.FROZEN_OBSIDIAN)
				{
					break;
				}
			}

			Block block = this.world.getBlockState(pos.offset(direction, i)).getBlock();
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
			label56: for (this.height = 0; this.height < 21; ++this.height)
			{
				for (int i = 0; i < this.width; ++i)
				{
					BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
					BlockState blockstate = this.world.getBlockState(blockpos);
					if (!this.isReplacable(blockstate))
					{
						break label56;
					}

					Block block = blockstate.getBlock();
					if (block == MubbleBlocks.PERMAFROST_PORTAL)
					{
						++this.portalBlockCount;
					}

					if (i == 0)
					{
						block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();
						if (block != MubbleBlocks.FROZEN_OBSIDIAN)
						{
							break label56;
						}
					}
					else if (i == this.width - 1)
					{
						block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();
						if (block != MubbleBlocks.FROZEN_OBSIDIAN)
						{
							break label56;
						}
					}
				}
			}

			for (int j = 0; j < this.width; ++j)
			{
				if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() != MubbleBlocks.FROZEN_OBSIDIAN)
				{
					this.height = 0;
					break;
				}
			}

			if (this.height <= 21 && this.height >= 3)
			{
				return this.height;
			}
			else
			{
				this.bottomLeft = null;
				this.width = 0;
				this.height = 0;
				return 0;
			}
		}

		protected boolean isReplacable(BlockState blockState)
		{
			Block block = blockState.getBlock();
			return block instanceof AirBlock || block == Blocks.FIRE || block == MubbleBlocks.PERMAFROST_PORTAL;
		}

		public boolean isValid()
		{
			return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
		}

		public void placePortalBlocks()
		{
			for (int i = 0; i < this.width; ++i)
			{
				BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);

				for (int j = 0; j < this.height; ++j)
				{
					this.world.setBlockState(blockpos.up(j),
					MubbleBlocks.PERMAFROST_PORTAL.getDefaultState().with(PermafrostPortalBlock.AXIS, this.axis), 18);
				}
			}
		}

		private boolean isRectangle()
		{
			return this.portalBlockCount >= this.width * this.height;
		}

		public boolean isCorrectPortal()
		{
			return this.isValid() && this.isRectangle();
		}
	}
}