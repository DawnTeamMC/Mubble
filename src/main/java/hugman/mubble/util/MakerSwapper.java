package hugman.mubble.util;

import javax.annotation.Nullable;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class MakerSwapper
{
    @Nullable
	public static Block getNextStyle(Block block)
    {
    	ResourceLocation previousID = block.getRegistryName();
    	for(Block i : MubbleBlocks.BLOCKS)
    	{
			ResourceLocation resultID = i.getRegistryName();
			String previousSID = previousID.getPath();
			String resultSID = resultID.getPath();
    		if(checkViability(previousID) && checkViability(resultID))
    		{
    			String temp;
    			temp = replace(previousSID, "nsmbu", "smb");
    				 if(replace(previousSID, "smb", "smb3")		== resultSID) return i;
    			else if(replace(previousSID, "smb3", "smw")		== resultSID) return i;
    			else if(replace(previousSID, "smw", "nsmbu")	== resultSID) return i;
    			else if(replace(previousSID, "nsmbu", "smb")	== resultSID) return i;
        		else continue;
    		}
    		else continue;
    	}
    	return null;
	}
    
    @Nullable
	public static Block getNextTheme(Block block)
    {
    	ResourceLocation previousID = block.getRegistryName();
    	for(Block i : MubbleBlocks.BLOCKS)
    	{
			ResourceLocation resultID = i.getRegistryName();
			String previousSID = previousID.getPath();
			String resultSID = resultID.getPath();
    		if(checkViability(previousID) && checkViability(resultID))
    		{
    				 if(replace(previousSID, "ground", "underground")		== resultSID) return i;
    			else if(replace(previousSID, "underground", "underwater")	== resultSID) return i;
    			else if(replace(previousSID, "underwater", "airship")		== resultSID) return i;
    			else if(replace(previousSID, "airship", "ghost_house")		== resultSID) return i;
    			else if(replace(previousSID, "ghost_house", "castle")		== resultSID) return i;
    			else if(replace(previousSID, "castle", "ground")			== resultSID) return i;
        		else continue;
    		}
    		else continue;
    	}
    	return null;
	}

    @Nullable
    public static String replace(String id, String from, String to)
    {
    	//if(id.matches("^(" + from + ")_.*"))
    		return id.replaceFirst(from, to);
    	//else return null;
	}
    
    public static boolean checkViability(ResourceLocation ID)
    {
    	if(ID.toString().matches("^(mubble:)(smb|smb3|smw|nsmbu)_(ground|underground|underwater|airship|ghost_house|castle)_.*")) return true;
    	else return false;
	}
}