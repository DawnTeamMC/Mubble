package hugman.mubble.object.block;

import hugman.mubble.init.data.MubbleTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.Stainable;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CloudBlock extends AbstractGlassBlock implements Stainable {
	private final DyeColor color;

	public CloudBlock(DyeColor colorIn) {
		super(FabricBlockSettings.of(Material.LEAVES, colorIn).sounds(BlockSoundGroup.WOOL).hardness(0f).noCollision());
		this.color = colorIn;
	}

	@Override
	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

	@Override
	public int getOpacity(BlockState state, BlockView worldIn, BlockPos pos) {
		return 0;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
		return false;
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		Vec3d vec3d = entityIn.getVelocity();
		if(entityIn instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) entityIn;
			ItemStack armor = entity.getEquippedStack(EquipmentSlot.HEAD);
			if(MubbleTags.Items.CROWNS.contains(armor.getItem()) && !entity.isSneaking()) {
				if(!entity.isSprinting()) {
					entity.setVelocity(vec3d.x, (entity.getRandom().nextInt(31) + 40) / 100D, vec3d.z);
				}
				else {
					entity.setVelocity(vec3d.x, 0.7D, vec3d.z);
				}
				entity.fallDistance = 0f;
			}
		}
		if(entityIn instanceof ItemEntity) {
			ItemEntity entity = (ItemEntity) entityIn;
			if(MubbleTags.Items.CROWNS.contains(entity.getStack().getItem())) {
				entity.setVelocity(vec3d.x, 0.3D, vec3d.z);
			}
			if(MubbleTags.Items.WEIGHT_LIGHT.contains(entity.getStack().getItem())) {
				entity.setVelocity(vec3d.x, 0.1D, vec3d.z);
			}
		}
	}
}
