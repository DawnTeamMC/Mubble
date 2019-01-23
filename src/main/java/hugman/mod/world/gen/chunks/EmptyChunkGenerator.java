package hugman.mod.world.gen.chunks;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public class EmptyChunkGenerator implements IChunkGenerator {
    private final World world;

    public EmptyChunkGenerator(World world)
    {
        this.world = world;
    }

    @Override
    public Chunk generateChunk(int x, int z)
    {
        return new Chunk(this.world, x, z);
    }

    @Override
    public void populate(int x, int z)
    {
    	return;
    }

    @Override
    public boolean generateStructures(Chunk chunk, int x, int z)
    {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean isInsideStructure(World world, String structureName, BlockPos pos)
    {
        return false;
    }

    @Override
    public BlockPos getNearestStructurePos(World world, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunk, int x, int z)
    {
    	return;
    }
}
