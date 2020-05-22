package hugman.mubble.objects.world.feature.structure;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

public class RedstoneShedStructure extends AbstractTempleFeature<DefaultFeatureConfig>
{
	public RedstoneShedStructure(Function<Dynamic<?>, ? extends DefaultFeatureConfig> config)
	{
		super(config);
	}
	
	@Override
	public String getName()
	{
		return "RedstoneShed";
	}
	
	@Override
	public int getRadius()
	{
		return 3;
	}
	
	@Override
	public StructureFeature.StructureStartFactory getStructureStartFactory()
	{
		return RedstoneShedStructure.Start::new;
	}
	
	@Override
	protected int getSeedModifier()
	{
		return 14357619;
	}
	
	public static class Start extends StructureStart
	{
		public Start(StructureFeature<?> structure, int chunkPosX, int chunkPosZ, BlockBox mutable, int references, long seed)
		{
			super(structure, chunkPosX, chunkPosZ, mutable, references, seed);
		}
		
		@Override
		public void initialize(ChunkGenerator<?> chunkGen, StructureManager tempManager, int posX, int posZ, Biome biome)
		{
			DefaultFeatureConfig nofeatureconfig = (DefaultFeatureConfig)chunkGen.getStructureConfig(biome, Feature.IGLOO);
			int i = posX * 16;
			int j = posZ * 16;
			BlockPos blockpos = new BlockPos(i, 90, j);
			BlockRotation rotation = BlockRotation.values()[this.random.nextInt(BlockRotation.values().length)];
			RedstoneShedPieces.place(tempManager, blockpos, rotation, this.children, this.random, nofeatureconfig);
			this.setBoundingBoxFromChildren();
		}
	}
}
