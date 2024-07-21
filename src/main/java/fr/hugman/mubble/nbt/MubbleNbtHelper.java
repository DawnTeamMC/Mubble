package fr.hugman.mubble.nbt;

import fr.hugman.mubble.Mubble;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class MubbleNbtHelper {
	public static BlockState toBlockStateNoWrapper(NbtCompound nbt) {
		if (!nbt.contains("Name", NbtElement.STRING_TYPE)) {
			return Blocks.AIR.getDefaultState();
		}
		Identifier identifier = new Identifier(nbt.getString("Name"));
		Block block = Registries.BLOCK.get(identifier);
		BlockState state = block.getDefaultState();
		if (nbt.contains("Properties", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("Properties");
			StateManager<Block, BlockState> stateManager = block.getStateManager();
			for (String string : nbtCompound.getKeys()) {
				net.minecraft.state.property.Property<?> property = stateManager.getProperty(string);
				if (property == null) continue;
				state = withProperty(state, property, string, nbtCompound, nbt);
			}
		}
		return state;
	}

	private static <S extends State<?, S>, T extends Comparable<T>> S withProperty(S state, net.minecraft.state.property.Property<T> property, String key, NbtCompound properties, NbtCompound root) {
		Optional<T> opt = property.parse(properties.getString(key));
		if (opt.isPresent()) {
			return (S) state.with(property, opt.get());
		}
		Mubble.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", key, properties.getString(key), root.toString());
		return state;
	}
}
