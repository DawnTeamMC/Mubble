package hugman.mod.util.interfaces;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.FMLCommonHandler;

public interface IStructure 
{
	public static final WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
	public static final PlacementSettings settings0 = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false).setMirror(Mirror.NONE).setRotation(Rotation.NONE);
	public static final PlacementSettings settings1 = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_90);
	public static final PlacementSettings settings2 = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_180);
	public static final PlacementSettings settings3 = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false).setMirror(Mirror.NONE).setRotation(Rotation.COUNTERCLOCKWISE_90);
	public static final PlacementSettings settings4 = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false).setMirror(Mirror.LEFT_RIGHT).setRotation(Rotation.NONE);
	public static final PlacementSettings settings5 = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false).setMirror(Mirror.LEFT_RIGHT).setRotation(Rotation.CLOCKWISE_90);
	public static final PlacementSettings settings6 = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false).setMirror(Mirror.LEFT_RIGHT).setRotation(Rotation.CLOCKWISE_180);
	public static final PlacementSettings settings7 = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false).setMirror(Mirror.LEFT_RIGHT).setRotation(Rotation.COUNTERCLOCKWISE_90);
}
