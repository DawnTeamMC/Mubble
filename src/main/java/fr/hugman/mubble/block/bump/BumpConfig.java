package fr.hugman.mubble.block.bump;

import fr.hugman.mubble.block.BumpableBlock;
import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * A class that contains the configuration of a {@link BumpableBlock}, and more furthermore of the {@linkplain fr.hugman.mubble.block.entity.BumpableBlockEntity block entity}.
 *
 * @param stack the item stack that will be dropped when the block is bumped
 * @param state the block state that will be set when the block is bumped. If set to {@code null}, the block will stay the same. If set to {@linkplain Blocks#AIR air}, the block will be broken.
 *
 * @author Hugman
 * @since 4.0.0
 */
public record BumpConfig(@Nullable ItemStack stack, @Nullable BlockState state) {
	private static final BlockState DESTROY_STATE = Blocks.AIR.getDefaultState();

	public static final BumpConfig NOTHING = new BumpConfig(null, null);
	public static final BumpConfig DESTROY = new BumpConfig(null, DESTROY_STATE);
	public static final BumpConfig EMPTY = BumpConfig.of(null, SuperMario.EMPTY_BLOCK);
	public static final BumpConfig DROP_COIN = BumpConfig.of(Items.GOLD_NUGGET, SuperMario.EMPTY_BLOCK);

	private static final String ITEM_STACK_KEY = "ItemStack";
	private static final String BLOCK_STATE_KEY = "BlockState";

	/*=============*/
	/*  FACTORIES  */
	/*=============*/

	public static BumpConfig of(ItemConvertible item, Block block) {
		return new BumpConfig(new ItemStack(item), block.getDefaultState());
	}

	public static BumpConfig of(ItemConvertible item, int itemCount, Block block) {
		return new BumpConfig(new ItemStack(item, itemCount), block.getDefaultState());
	}

	/*============*/
	/*  BEHAVIOR  */
	/*============*/

	public boolean shouldDestroy() {
		return this.state() == DESTROY_STATE;
	}

	/*=======*/
	/*  NBT  */
	/*=======*/

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		if(stack != null) {
			nbt.put(ITEM_STACK_KEY, this.stack.writeNbt(new NbtCompound()));
		}
		if(state != null) {
			nbt.put(BLOCK_STATE_KEY, NbtHelper.fromBlockState(this.state));
		}
		return nbt;
	}

	public static BumpConfig fromNbt(NbtCompound nbt, World world) {
		ItemStack stack = null;
		if(nbt.contains(ITEM_STACK_KEY)) {
			stack = ItemStack.fromNbt(nbt.getCompound(ITEM_STACK_KEY));
		}
		BlockState state = null;
		if(nbt.contains(BLOCK_STATE_KEY)) {
			RegistryWrapper<Block> registry = world.createCommandRegistryWrapper(RegistryKeys.BLOCK);
			state = NbtHelper.toBlockState(registry, nbt.getCompound(BLOCK_STATE_KEY));
		}
		return new BumpConfig(stack, state);
	}

	public BumpConfig withStack(ItemStack stack) {
		return new BumpConfig(stack, this.state);
	}

	public BumpConfig copy() {
		var stack = this.stack;
		if(stack != null) {
			stack = stack.copy();
		}
		return new BumpConfig(stack, this.state);
	}
}