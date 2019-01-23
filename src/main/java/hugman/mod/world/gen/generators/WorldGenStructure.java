package hugman.mod.world.gen.generators;

import java.util.Random;

import hugman.mod.util.Reference;
import hugman.mod.util.interfaces.IStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenStructure extends WorldGenerator implements IStructure
{
	public String structureName;
	
	public WorldGenStructure(String name) 
	{
		this.structureName = name;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) 
	{
		this.generateStructure(worldIn, position);
		return true;
	}
	
	public void generateStructure(World world, BlockPos pos)
	{
		MinecraftServer mcServer = world.getMinecraftServer();
		TemplateManager manager = worldServer.getStructureTemplateManager();
		ResourceLocation location = new ResourceLocation(Reference.MODID, structureName);
		Template template = manager.get(mcServer, location);
		
		if(template != null)
		{
			IBlockState state = world.getBlockState(pos);
	        Random rand = new Random();
			world.notifyBlockUpdate(pos, state, state, 3);
			int settings = rand.nextInt(8);
			if(settings == 0) template.addBlocksToWorldChunk(world, pos, settings0);
			else if(settings == 1) template.addBlocksToWorldChunk(world, pos, settings1);
			else if(settings == 2) template.addBlocksToWorldChunk(world, pos, settings2);
			else if(settings == 3) template.addBlocksToWorldChunk(world, pos, settings3);
			else if(settings == 4) template.addBlocksToWorldChunk(world, pos, settings4);
			else if(settings == 5) template.addBlocksToWorldChunk(world, pos, settings5);
			else if(settings == 6) template.addBlocksToWorldChunk(world, pos, settings6);
			else if(settings == 7) template.addBlocksToWorldChunk(world, pos, settings7);
		}
	}
}