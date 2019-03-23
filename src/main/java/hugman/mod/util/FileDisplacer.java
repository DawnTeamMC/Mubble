package hugman.mod.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.client.Minecraft;

public class FileDisplacer
{
	public static void copyToDimension(String dir, String file, int dim)
	{
		InputStream FROM = FileDisplacer.class.getClassLoader().getResourceAsStream("assets/mubble/worlds/" + dir + "/" + file);
	    Path TO = new File(Minecraft.getInstance().gameDir + "/saves/" + Minecraft.getInstance().getIntegratedServer().getFolderName(), "/DIM" + dim + "/" + file).toPath();
	    CopyOption[] options = new CopyOption[] { };
	    Path parentDir = TO.getParent();
	    if (Files.exists(TO)) return;
	    try
	    {
		    if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
	    	Files.copy(FROM, TO, options);
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}
}