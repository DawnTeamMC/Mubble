package hugman.mubble.objects;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import net.minecraft.util.ResourceLocation;

public class Shaders
{
    public static final List<ResourceLocation> SHADERS = new ArrayList<ResourceLocation>();
    public static final List<ResourceLocation> RETRO_SHADERS = new ArrayList<ResourceLocation>();
    
	public static final ResourceLocation ANTIALIAS = register("minecraft", "antialias");
	public static final ResourceLocation ART = register("minecraft", "art");
	public static final ResourceLocation BITS = register("minecraft", "bits");
	public static final ResourceLocation BLOBS = register("minecraft", "blobs");
	public static final ResourceLocation BLOBS2 = register("minecraft", "blobs2");
	public static final ResourceLocation BLUR = register("minecraft", "blur");
	public static final ResourceLocation BUMPY = register("minecraft", "bumpy");
	public static final ResourceLocation COLOR_CONVOLVE = register("minecraft", "color_convolve");
	public static final ResourceLocation CREEPER = register("minecraft", "creeper");
	public static final ResourceLocation DECONVERGE = register("minecraft", "deconverge");
	public static final ResourceLocation DESATURATE = register("minecraft", "desaturate");
	public static final ResourceLocation ENTITY_OUTLINE = register("minecraft", "entity_outline");
	public static final ResourceLocation FLIP = register("minecraft", "flip");
	public static final ResourceLocation FXAA = register("minecraft", "fxaa");
	public static final ResourceLocation GREEN = register("minecraft", "green");
	public static final ResourceLocation INVERT = register("minecraft", "invert");
	public static final ResourceLocation NOTCH = register("minecraft", "notch");
	public static final ResourceLocation NTSC = register("minecraft", "ntsc");
	public static final ResourceLocation OUTLINE = register("minecraft", "outline");
	public static final ResourceLocation PENCIL = register("minecraft", "pencil");
	public static final ResourceLocation PHOSPHOR = register("minecraft", "phosphor");
	public static final ResourceLocation SCAN_PINCUSHION = register("minecraft", "scan_pincushion");
	public static final ResourceLocation SOBEL = register("minecraft", "sobel");
	public static final ResourceLocation SPIDER = register("minecraft", "spider");
	public static final ResourceLocation WOBBLE = register("minecraft", "wobble");

	public static final ResourceLocation WHITE_RETRO = register(Mubble.MOD_ID, "retro/white", RETRO_SHADERS);
	public static final ResourceLocation LIGHT_GRAY_RETRO = register(Mubble.MOD_ID, "retro/light_gray", RETRO_SHADERS);
	public static final ResourceLocation GRAY_RETRO = register(Mubble.MOD_ID, "retro/gray", RETRO_SHADERS);
	public static final ResourceLocation BLACK_RETRO = register(Mubble.MOD_ID, "retro/black", RETRO_SHADERS);
	public static final ResourceLocation BROWN_RETRO = register(Mubble.MOD_ID, "retro/brown", RETRO_SHADERS);
	public static final ResourceLocation RED_RETRO = register(Mubble.MOD_ID, "retro/red", RETRO_SHADERS);
	public static final ResourceLocation ORANGE_RETRO = register(Mubble.MOD_ID, "retro/orange", RETRO_SHADERS);
	public static final ResourceLocation YELLOW_RETRO = register(Mubble.MOD_ID, "retro/yellow", RETRO_SHADERS);
	public static final ResourceLocation LIME_RETRO = register(Mubble.MOD_ID, "retro/lime", RETRO_SHADERS);
	public static final ResourceLocation GREEN_RETRO = register(Mubble.MOD_ID, "retro/green", RETRO_SHADERS);
	public static final ResourceLocation CYAN_RETRO = register(Mubble.MOD_ID, "retro/cyan", RETRO_SHADERS);
	public static final ResourceLocation LIGHT_BLUE_RETRO = register(Mubble.MOD_ID, "retro/light_blue", RETRO_SHADERS);
	public static final ResourceLocation BLUE_RETRO = register(Mubble.MOD_ID, "retro/blue", RETRO_SHADERS);
	public static final ResourceLocation PURPLE_RETRO = register(Mubble.MOD_ID, "retro/purple", RETRO_SHADERS);
	public static final ResourceLocation MAGENTA_RETRO = register(Mubble.MOD_ID, "retro/magenta", RETRO_SHADERS);
	public static final ResourceLocation PINK_RETRO = register(Mubble.MOD_ID, "retro/pink", RETRO_SHADERS);
	
	private static ResourceLocation register(String namespace, String name)
	{
		ResourceLocation shaderRL = new ResourceLocation(namespace, "shaders/post/" + name + ".json");
		SHADERS.add(shaderRL);
		return shaderRL;
	}
	
	private static ResourceLocation register(String namespace, String name, List<ResourceLocation> aditionalList)
	{
		ResourceLocation shaderRL = new ResourceLocation(namespace, "shaders/post/" + name + ".json");
		SHADERS.add(shaderRL);
		aditionalList.add(shaderRL);
		return shaderRL;
	}
}