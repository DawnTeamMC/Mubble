package hugman.mubble.objects.block;

import javax.annotation.Nullable;

import hugman.mubble.objects.entity.CustomTNTEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CustomTNTBlock extends Block
{
	public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
	public final int fuse;
	public final float strenght;
	
    public CustomTNTBlock(Block.Properties builder, int fuseIn, float strenghtIn)
    {
        super(builder);
        fuse = fuseIn;
        strenght = strenghtIn;
        this.setDefaultState(this.getDefaultState().with(UNSTABLE, Boolean.valueOf(false)));
    }
	
    public CustomTNTBlock(Block.Properties builder, float multiplier)
    {
        super(builder);
        fuse = Math.round(80.0F * (multiplier * 0.75F));
        strenght = 4.0F * multiplier;
        this.setDefaultState(this.getDefaultState().with(UNSTABLE, Boolean.valueOf(false)));
    }
    
    @Override
    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter)
    {
    	explode(world, pos, igniter);
    }
    
    @Override
	public void onBlockAdded(BlockState state1, World world, BlockPos pos, BlockState state2, boolean p_220082_5_)
	{
		if (state2.getBlock() != state1.getBlock())
		{
			if (world.isBlockPowered(pos))
			{
				catchFire(state1, world, pos, null, null);
				world.removeBlock(pos, false);
			}
		}
	}
	
	@Override
	public void neighborChanged(BlockState state1, World world, BlockPos pos1, Block block, BlockPos pos2, boolean p_220069_6_)
	{
		if (world.isBlockPowered(pos1))
		{
			catchFire(state1, world, pos1, null, null);
			world.removeBlock(pos1, false);
		}
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		if(!world.isRemote() && !player.isCreative() && state.get(UNSTABLE))
		{
			catchFire(state, world, pos, null, player);
		}
		super.onBlockHarvested(world, pos, state, player);
	}

	@Override
	public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion)
	{
		if(!world.isRemote)
		{
			CustomTNTEntity tntentity = new CustomTNTEntity(this.getDefaultState(), world, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), fuse, strenght, explosion.getExplosivePlacedBy());
			tntentity.setFuse((short) (world.rand.nextInt(tntentity.getFuse() / 4) + tntentity.getFuse() / 8));
			world.addEntity(tntentity);
		}
	}
	
	private void explode(World world, BlockPos pos, @Nullable LivingEntity igniter)
	{
		if(!world.isRemote)
		{
			CustomTNTEntity tntentity = new CustomTNTEntity(this.getDefaultState(), world, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, fuse, strenght, igniter);
			world.addEntity(tntentity);
			world.playSound((PlayerEntity) null, tntentity.getX(), tntentity.getY(), tntentity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result)
	{
		ItemStack itemstack = player.getHeldItem(hand);
		Item item = itemstack.getItem();
		if(item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE)
		{
			return super.onUse(state, world, pos, player, hand, result);
		}
		else
		{
			catchFire(state, world, pos, result.getFace(), player);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
			if(!player.isCreative())
			{
				if(item == Items.FLINT_AND_STEEL)
				{
					itemstack.damageItem(1, player, (entity) -> 
					{
						entity.sendBreakAnimation(hand);
					});
				}
				else
				{
					itemstack.shrink(1);
				}
			}

			return ActionResultType.SUCCESS;
		}
	}
	
	@Override
	public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult result, Entity entityIn) {
		if(!world.isRemote && entityIn instanceof AbstractArrowEntity)
		{
			AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity) entityIn;
			Entity shooter = abstractarrowentity.getShooter();
			if (abstractarrowentity.isBurning())
			{
				BlockPos blockpos = result.getPos();
				catchFire(state, world, blockpos, null, shooter instanceof LivingEntity ? (LivingEntity) shooter : null);
				world.removeBlock(blockpos, false);
			}
		}

	}
	
	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(UNSTABLE);
	}
}