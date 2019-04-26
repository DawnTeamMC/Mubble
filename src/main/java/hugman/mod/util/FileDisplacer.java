package hugman.mod.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import hugman.mod.Mubble;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class FileDisplacer
{
	
	public static void createUltimatumWorldFiles(IReloadableResourceManager resourceManager)
	{
		String folder = "ultimatum_world";
		String extension = ".txt";
		
		for (ResourceLocation file : resourceManager.getAllResourceLocations(folder, n -> n.endsWith(extension)))
		{
	    	Mubble.getLogger().info("[mubble/ultimatum] Found file {}", file);
		    try (IResource iresource = resourceManager.getResource(file))
		    {
		    	Path destination = new File(Minecraft.getInstance().gameDir + "/saves/" + Minecraft.getInstance().getIntegratedServer().getFolderName(), "/mubble/ultimatum/region/" + file.getPath() + ".txt").toPath();
			    CopyOption[] options = new CopyOption[] { };
			    Path parentDir = destination.getParent();
			    if(Files.exists(destination)) return;
			    try
			    {
				    if(!Files.exists(parentDir)) Files.createDirectories(parentDir);
			    	Files.copy(iresource.getInputStream(), destination, options);
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