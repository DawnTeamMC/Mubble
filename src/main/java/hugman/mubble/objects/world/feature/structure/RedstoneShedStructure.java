package hugman.mubble.objects.world.feature.structure;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class RedstoneShedStructure extends ScatteredStructure<NoFeatureConfig>
{
	public RedstoneShedStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
	{
		super(config);
	}
	
	@Override
	public String getStructureName()
	{
		return "RedstoneShed";
	}
	
	@Override
	public int getSize()
	{
		return 3;
	}
	
	@Override
	public IStartFactory getStartFactory()
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
		public Start(Structure<?> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox mutable, int references, long seed)
		{
			super(structure, chunkPosX, chunkPosZ, mutable, references, seed);
		}
		
		@Override
		public void init(ChunkGenerator<?> chunkGen, TemplateManager tempManager, int posX, int posZ, Biome biome)
		{
			NoFeatureConfig nofeatureconfig = (NoFeatureConfig)chunkGen.getStructureConfig(biome, Feature.IGLOO);
			int i = posX * 16;
			int j = posZ * 16;
			BlockPos blockpos = new BlockPos(i, 90, j);
			Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
			RedstoneShedPieces.place(tempManager, blockpos, rotation, this.components, this.rand, nofeatureconfig);
			this.recalculateStructureSize();
		}
	}
}
