package hugman.mubble.init.data;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class MubbleKeyBindings 
{
    public static final List<KeyBinding> KEY_BINDINGS = new ArrayList<KeyBinding>();
    
	public static final KeyBinding OUTFIT_KEY = register("outfit", GLFW.GLFW_KEY_K);
	
	private static KeyBinding register(String name, int key)
	{
		KeyBinding keyBinding = new KeyBinding("key.mubble." + name + ".desc", key, "key.categories.gameplay");
		KEY_BINDINGS.add(keyBinding);
		return keyBinding;
	}
	
	public static void registerKeyBindings()
	{
		for(KeyBinding keyBinding : KEY_BINDINGS)
		{
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}
}