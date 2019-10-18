package hugman.mubble.util;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import net.minecraft.util.ResourceLocation;

public class Shaders
{
    public static final List<ResourceLocation> SHADERS = new ArrayList<ResourceLocation>();
    
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

	public static final ResourceLocation WHITE_RETRO = register(Mubble.MOD_ID, "retro/white");
	public static final ResourceLocation LIGHT_GRAY_RETRO = register(Mubble.MOD_ID, "retro/light_gray");
	public static final ResourceLocation GRAY_RETRO = register(Mubble.MOD_ID, "retro/gray");
	public static final ResourceLocation BLACK_RETRO = register(Mubble.MOD_ID, "retro/black");
	public static final ResourceLocation BROWN_RETRO = register(Mubble.MOD_ID, "retro/brown");
	public static final ResourceLocation RED_RETRO = register(Mubble.MOD_ID, "retro/red");
	public static final ResourceLocation ORANGE_RETRO = register(Mubble.MOD_ID, "retro/orange");
	public static final ResourceLocation YELLOW_RETRO = register(Mubble.MOD_ID, "retro/yellow");
	public static final ResourceLocation LIME_RETRO = register(Mubble.MOD_ID, "retro/lime");
	public static final ResourceLocation GREEN_RETRO = register(Mubble.MOD_ID, "retro/green");
	public static final ResourceLocation CYAN_RETRO = register(Mubble.MOD_ID, "retro/cyan");
	public static final ResourceLocation LIGHT_BLUE_RETRO = register(Mubble.MOD_ID, "retro/light_blue");
	public static final ResourceLocation BLUE_RETRO = register(Mubble.MOD_ID, "retro/blue");
	public static final ResourceLocation PURPLE_RETRO = register(Mubble.MOD_ID, "retro/purple");
	public static final ResourceLocation MAGENTA_RETRO = register(Mubble.MOD_ID, "retro/magenta");
	public static final ResourceLocation PINK_RETRO = register(Mubble.MOD_ID, "retro/pink");
	
	private static ResourceLocation register(String namespace, String name)
	{
		ResourceLocation shaderRL = new ResourceLocation(namespace, "shaders/post/" + name + ".json");
		SHADERS.add(shaderRL);
		return shaderRL;
	}
}