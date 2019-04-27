package hugman.mod.util;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hugman.mod.Mubble;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class FileDisplacer
{
	public static void createUltimatumWorldFiles(MinecraftServer server)
	{
		IResourceManager resourceManager = server.getResourceManager();
		String folder = "ultimatum_world";
		String extension = ".mca";
		
		for (ResourceLocation file : resourceManager.getAllResourceLocations(folder, n -> n.endsWith(extension)))
		{
		    try (IResource iresource = resourceManager.getResource(file))
		    {
		    	Path destination;
		    	if(server.isDedicatedServer()) destination = server.getDataDirectory().toPath().resolve(Paths.get(server.getFolderName(), "mubble", "ultimatum", "region", file.getPath().substring(folder.length() + 1)));
		    	else destination = server.getDataDirectory().toPath().resolve(Paths.get("saves", server.getFolderName(), "mubble", "ultimatum", "region", file.getPath().substring(folder.length() + 1)));
			    CopyOption[] options = new CopyOption[] {};
			    Path parentDir = destination.getParent();
			    if(Files.exists(destination)) continue;
			    try
			    {
				    if(!Files.exists(parentDir)) Files.createDirectories(parentDir);
			    	Files.copy(iresource.getInputStream(), destination, options);
			    	Mubble.LOGGER.info("Succesfully copied file {} for Ultimatum", file.getPath().substring(folder.length() + 1));
			    }
			    catch(IOException e)
			    {
			        e.printStackTrace();
			    }
		    }
		    catch (IOException e1)
		    {
				e1.printStackTrace();
			}
		}
	}
}