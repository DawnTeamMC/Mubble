package fr.hugman.mubble.zelda.world.level.block;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.clock.ClockTimeMarker;
import net.minecraft.world.clock.ClockTimeMarkers;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * One stage of a loftwing egg. Each stage is a block of its own, the way copper weathers, and each
 * one looks at the sun: at noon it has one chance in twenty of moving on, which works out at about
 * twenty days from laying to hatching.
 * <p>
 * The roll is driven by a scheduled tick aimed at midday rather than by random ticks, so an egg in
 * a loaded chunk keeps exact time and an egg saved to disk picks up where it left off.
 */
public class LoftwingEggBlock extends Block {
	public static final MapCodec<LoftwingEggBlock> CODEC = simpleCodec(properties -> new LoftwingEggBlock(properties, null));

	/** One in twenty per midday: roughly twenty days from laying to hatching. */
	public static final float HATCH_CHANCE = 0.05F;

	private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0);

	/** The stage after this one, or nothing at all when this is the one that hatches. */
	@Nullable
	private final Supplier<Block> nextStage;

	public LoftwingEggBlock(BlockBehaviour.Properties properties, @Nullable Supplier<Block> nextStage) {
		super(properties);
		this.nextStage = nextStage;
	}

	@Override
	public MapCodec<LoftwingEggBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(state, level, pos, oldState, moving);
		scheduleNextNoon(level, pos, this);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (random.nextFloat() >= HATCH_CHANCE) {
			scheduleNextNoon(level, pos, this);
			return;
		}
		if (this.nextStage != null) {
			// The next stage schedules its own midday from onPlace.
			level.setBlockAndUpdate(pos, this.nextStage.get().defaultBlockState());
			level.playSound(null, pos, ZeldaSounds.LOFTWING_EGG_CRACK, SoundSource.BLOCKS, 1.0F, 1.0F);
			return;
		}
		hatch(level, pos);
	}

	private static void hatch(ServerLevel level, BlockPos pos) {
		level.removeBlock(pos, false);
		level.playSound(null, pos, ZeldaSounds.LOFTWING_EGG_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);

		Loftwing chick = ZeldaEntityTypes.LOFTWING.create(level, EntitySpawnReason.BREEDING);
		if (chick == null) {
			return;
		}
		chick.setBaby(true);
		chick.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
		LoftwingVariant.pickAtRandom(level.registryAccess().lookupOrThrow(ZeldaRegistries.LOFTWING_VARIANT), level.getRandom()).ifPresent(chick::setVariant);
		level.addFreshEntity(chick);
	}

	/**
	 * Aims a tick at the next midday, whether that is later today or tomorrow. Noon is read from
	 * the game's own clock markers, so an egg follows whatever a data pack calls midday.
	 */
	private static void scheduleNextNoon(Level level, BlockPos pos, Block block) {
		ClockTimeMarker noon = level.registryAccess().lookupOrThrow(ClockTimeMarkers.ROOT_ID).getOrThrow(ClockTimeMarkers.NOON).value();
		long now = level.clockManager().getTotalTicks(noon.clock());
		long delay = noon.resolveTimeToMoveTo(now) - now;
		level.scheduleTick(pos, block, (int) Math.max(1L, delay));
	}
}
