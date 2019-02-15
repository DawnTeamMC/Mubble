package hugman.mod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.Dimension;

public class Teleporter extends net.minecraft.world.Teleporter
{
	private final WorldServer world;
    private double x, y, z;
	
	public Teleporter(WorldServer world, double x, double y, double z) 
	{
		super(world);
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void placeInPortal(Entity entity, float rotationYaw) 
	{
		this.world.getBlockState(new BlockPos((int) this.x, (int) this.y, (int) this.z));
        entity.setPosition(this.x, this.y, this.z);
        entity.motionX = 0.0f;
        entity.motionY = 0.0f;
        entity.motionZ = 0.0f;
	}
	
	public static void teleportToDimension(EntityPlayer player, Dimension dimension, double x, double y, double z) 
	{
        Dimension oldDimension = player.getEntityWorld().getDimension();
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
        MinecraftServer server = player.getEntityWorld().getServer();
        WorldServer worldServer = server.getWorld(dimension.getType());
        player.addExperienceLevel(0);

        if (worldServer == null || worldServer.getServer() == null)
        {
            throw new IllegalArgumentException("Dimension: " + dimension + " doesn't exist!");
        }

        worldServer.getServer().getPlayerList().changePlayerDimension(entityPlayerMP, dimension.getType(), new Teleporter(worldServer, x, y, z));;
        player.setPositionAndUpdate(x, y, z);
    }
}