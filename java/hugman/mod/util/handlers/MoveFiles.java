package hugman.mod.util.handlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.client.Minecraft;

public final class MoveFiles
{	
	public static void copyToWorld(String dir, String file, int dim) throws IOException
	{
		InputStream FROM = MoveFiles.class.getClassLoader().getResourceAsStream("assets/mubble/worlds/" + dir + "/" + file);
	    Path TO = new File(Minecraft.getMinecraft().gameDir + "/saves/" + Minecraft.getMinecraft().getIntegratedServer().getFolderName(), "/DIM" + dim + "/" + file).toPath();
	    CopyOption[] options = new CopyOption[] { };
	    Path parentDir = TO.getParent();
	    if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
	    if (Files.exists(TO)) return;
	    
	    Files.copy(FROM, TO, options);
	}
}