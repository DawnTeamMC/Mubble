package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.dimension.PermafrostDimension;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;

public class MubbleDimensions
{
	private static EntityPlacer PLACER = (entity, world, dim, offsetX, offsetZ) -> new BlockPattern.TeleportTarget(new Vec3d(entity.getBlockPos().getX(), world.getChunk(entity.getBlockPos().getX() >> 4, entity.getBlockPos().getZ() >> 4).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, entity.getBlockPos().getX() & 15, entity.getBlockPos().getZ() & 15) + 1, entity.getBlockPos().getZ()), entity.getVelocity(), (int) entity.yaw);
	
	public final static FabricDimensionType PERMAFROST = FabricDimensionType.builder()
			.skyLight(false)
			.biomeAccessStrategy(VoronoiBiomeAccessType.INSTANCE)
			.defaultPlacer(PLACER)
			.factory(PermafrostDimension::new)
			.buildAndRegister(new Identifier(Mubble.MOD_ID, "permafrost"));
}