package hugman.mubble.objects.block;

import java.util.Random;

import com.google.common.cache.LoadingCache;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.world.MubbleDimensions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PermafrostPortalBlock extends Block
{
	public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
	protected static final VoxelShape X_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
	protected static final VoxelShape Z_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

	public PermafrostPortalBlock(Settings builder)
	{
		super(builder);
		this.setDefaultState(this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X));
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder)
	{
		builder.add(AXIS);
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context) 
	{
		switch((Direction.Axis)state.get(AXIS))
		{
			case Z:
				return Z_SHAPE;
			case X:
			default:
				return X_SHAPE;
		}
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rot)
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
	public void scheduledTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if(worldIn.dimension.hasVisibleSky() && worldIn.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && random.nextInt(2000) < worldIn.getDifficulty().getId())
		{
			while(worldIn.getBlockState(pos).getBlock() == this)
			{
				pos = pos.down();
			}
			
			if(worldIn.getBlockState(pos).allowsSpawning(worldIn, pos, EntityType.ZOMBIE_PIGMAN))
			{
				Entity entity = MubbleEntities.ZOMBIE_COWMAN.spawn(worldIn, (CompoundTag) null, (Text) null, (PlayerEntity) null, pos.up(), SpawnType.STRUCTURE, false, false);
				if (entity != null)
				{
					entity.netherPortalCooldown = entity.getDefaultNetherPortalCooldown();
				}
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(rand.nextInt(100) == 0)
		{
			worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}

		for(int i = 0; i < 4; ++i)
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
				d3 = (double) (rand.nextFloat() * 0.3F * (float) j);
			} else
			{
				d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
				d5 = (double) (rand.nextFloat() * 0.3F * (float) j);
			}

			if(rand.nextInt(10) == 0) worldIn.addParticle(ParticleTypes.CLOUD, d0, d1, d2, d3, d4, d5);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{
		if(!entity.hasVehicle() && !entity.hasPassengers() && !(entity instanceof EnderDragonEntity) && !(entity instanceof WitherEntity))
		{
			entity.changeDimension(MubbleDimensions.PERMAFROST);
			/*if(entity.netherPortalCooldown > 0)
			{
				entity.netherPortalCooldown = entity.getDefaultNetherPortalCooldown();
			}
			else
			{
				if(!world.isClient && !pos.equals(entity.lastNetherPortalPosition))
				{
					entity.lastNetherPortalPosition = new BlockPos(pos);
					BlockPattern.Result patternHelper = PermafrostPortalBlock.createPatternHelper(entity.world, entity.lastNetherPortalPosition);
					double d0 = patternHelper.getForwards().getAxis() == Direction.Axis.X ? (double) patternHelper.getFrontTopLeft().getZ() : (double) patternHelper.getFrontTopLeft().getX();
					double d1 = Math.abs(MathHelper.minusDiv((patternHelper.getForwards().getAxis() == Direction.Axis.X ? entity.getZ() : entity.getX()) - (double) (patternHelper.getForwards().rotateYClockwise().getDirection() == Direction.AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - (double) patternHelper.getWidth()));
					double d2 = MathHelper.minusDiv(entity.getY() - 1.0D, (double) patternHelper.getFrontTopLeft().getY(), (double) (patternHelper.getFrontTopLeft().getY() - patternHelper.getHeight()));
					entity.lastNetherPortalDirectionVector = new Vec3d(d1, d2, 0.0D);
					entity.lastNetherPortalDirection = patternHelper.getForwards();
					entity.changeDimension(world.dimension.getType() == MubbleDimensions.PERMAFROST ? DimensionType.OVERWORLD : MubbleDimensions.PERMAFROST);
				}

				entity.inNetherPortal = true;
			}*/
		}
	}
	
	public boolean trySpawnPortal(IWorld worldIn, BlockPos pos) 
	{
		PermafrostPortalBlock.Size size = this.isPortal(worldIn, pos);
		if (size != null)
		{
			size.placePortalBlocks();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public PermafrostPortalBlock.Size isPortal(IWorld worldIn, BlockPos pos)
	{
		PermafrostPortalBlock.Size sizeX = new PermafrostPortalBlock.Size(worldIn, pos, Direction.Axis.X);
		if (sizeX.isValid() && sizeX.portalBlockCount == 0)
		{
			return sizeX;
		}
		else
		{
			PermafrostPortalBlock.Size sizeZ = new PermafrostPortalBlock.Size(worldIn, pos, Direction.Axis.Z);
			return sizeZ.isValid() && sizeZ.portalBlockCount == 0 ? sizeZ : null;
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		Direction.Axis facingAxis = facing.getAxis();
		Direction.Axis stateAxis = stateIn.get(AXIS);
		boolean flag = stateAxis != facingAxis && facingAxis.isHorizontal();
		return !flag && facingState.getBlock() != this && !(new PermafrostPortalBlock.Size(worldIn, currentPos, stateAxis)).isCorrectPortal() ? Blocks.AIR.getDefaultState() : stateIn;
	}

	public static BlockPattern.Result createPatternHelper(IWorld worldIn, BlockPos pos)
	{
		Direction.Axis direction$axis = Direction.Axis.Z;
		PermafrostPortalBlock.Size size = new PermafrostPortalBlock.Size(worldIn, pos, Direction.Axis.X);
		LoadingCache<BlockPos, CachedBlockPosition> loadingcache = BlockPattern.makeCache(worldIn, true);
		if (!size.isValid())
		{
			direction$axis = Direction.Axis.X;
			size = new PermafrostPortalBlock.Size(worldIn, pos, Direction.Axis.Z);
		}

		if (!size.isValid())
		{
			return new BlockPattern.Result(pos, Direction.NORTH, Direction.UP, loadingcache, 1, 1, 1);
		}
		else
		{
			int[] aint = new int[Direction.AxisDirection.values().length];
			Direction direction = size.rightDir.rotateYCounterclockwise();
			BlockPos blockpos = size.bottomLeft.up(size.getHeight() - 1);

			for (Direction.AxisDirection direction$axisdirection : Direction.AxisDirection.values())
			{
				BlockPattern.Result blockpattern$patternhelper = new BlockPattern.Result(direction.getDirection() == direction$axisdirection ? blockpos : blockpos.offset(size.rightDir, size.getWidth() - 1), Direction.get(direction$axisdirection, direction$axis), Direction.UP, loadingcache, size.getWidth(), size.getHeight(), 1);

				for (int i = 0; i < size.getWidth(); ++i)
				{
					for (int j = 0; j < size.getHeight(); ++j)
					{
						CachedBlockPosition cachedblockinfo = blockpattern$patternhelper.translate(i, j, 1);
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

			return new BlockPattern.Result(direction.getDirection() == direction$axisdirection1 ? blockpos : blockpos.offset(size.rightDir, size.getWidth() - 1), Direction.get(direction$axisdirection1, direction$axis), Direction.UP, loadingcache, size.getWidth(), size.getHeight(), 1);
		}
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
					this.world.setBlockState(blockpos.up(j), MubbleBlocks.PERMAFROST_PORTAL.getDefaultState().with(PermafrostPortalBlock.AXIS, this.axis), 18);
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