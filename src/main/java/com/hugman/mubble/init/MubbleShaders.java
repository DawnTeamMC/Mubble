package com.hugman.mubble.init;

import com.hugman.mubble.Mubble;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MubbleShaders {
	public static final List<Identifier> SHADERS = new ArrayList<>();
	public static final List<Identifier> RETRO_SHADERS = new ArrayList<>();

	public static final Identifier ANTIALIAS = register("minecraft", "antialias");
	public static final Identifier ART = register("minecraft", "art");
	public static final Identifier BITS = register("minecraft", "bits");
	public static final Identifier BLOBS = register("minecraft", "blobs");
	public static final Identifier BLOBS2 = register("minecraft", "blobs2");
	public static final Identifier BLUR = register("minecraft", "blur");
	public static final Identifier BUMPY = register("minecraft", "bumpy");
	public static final Identifier COLOR_CONVOLVE = register("minecraft", "color_convolve");
	public static final Identifier CREEPER = register("minecraft", "creeper");
	public static final Identifier DECONVERGE = register("minecraft", "deconverge");
	public static final Identifier DESATURATE = register("minecraft", "desaturate");
	public static final Identifier ENTITY_OUTLINE = register("minecraft", "entity_outline");
	public static final Identifier FLIP = register("minecraft", "flip");
	public static final Identifier FXAA = register("minecraft", "fxaa");
	public static final Identifier GREEN = register("minecraft", "green");
	public static final Identifier INVERT = register("minecraft", "invert");
	public static final Identifier NOTCH = register("minecraft", "notch");
	public static final Identifier NTSC = register("minecraft", "ntsc");
	public static final Identifier OUTLINE = register("minecraft", "outline");
	public static final Identifier PENCIL = register("minecraft", "pencil");
	public static final Identifier PHOSPHOR = register("minecraft", "phosphor");
	public static final Identifier SCAN_PINCUSHION = register("minecraft", "scan_pincushion");
	public static final Identifier SOBEL = register("minecraft", "sobel");
	public static final Identifier SPIDER = register("minecraft", "spider");
	public static final Identifier WOBBLE = register("minecraft", "wobble");

	public static final Identifier WHITE_RETRO = register(Mubble.MOD_ID, "retro/white", RETRO_SHADERS);
	public static final Identifier LIGHT_GRAY_RETRO = register(Mubble.MOD_ID, "retro/light_gray", RETRO_SHADERS);
	public static final Identifier GRAY_RETRO = register(Mubble.MOD_ID, "retro/gray", RETRO_SHADERS);
	public static final Identifier BLACK_RETRO = register(Mubble.MOD_ID, "retro/black", RETRO_SHADERS);
	public static final Identifier BROWN_RETRO = register(Mubble.MOD_ID, "retro/brown", RETRO_SHADERS);
	public static final Identifier RED_RETRO = register(Mubble.MOD_ID, "retro/red", RETRO_SHADERS);
	public static final Identifier ORANGE_RETRO = register(Mubble.MOD_ID, "retro/orange", RETRO_SHADERS);
	public static final Identifier YELLOW_RETRO = register(Mubble.MOD_ID, "retro/yellow", RETRO_SHADERS);
	public static final Identifier LIME_RETRO = register(Mubble.MOD_ID, "retro/lime", RETRO_SHADERS);
	public static final Identifier GREEN_RETRO = register(Mubble.MOD_ID, "retro/green", RETRO_SHADERS);
	public static final Identifier CYAN_RETRO = register(Mubble.MOD_ID, "retro/cyan", RETRO_SHADERS);
	public static final Identifier LIGHT_BLUE_RETRO = register(Mubble.MOD_ID, "retro/light_blue", RETRO_SHADERS);
	public static final Identifier BLUE_RETRO = register(Mubble.MOD_ID, "retro/blue", RETRO_SHADERS);
	public static final Identifier PURPLE_RETRO = register(Mubble.MOD_ID, "retro/purple", RETRO_SHADERS);
	public static final Identifier MAGENTA_RETRO = register(Mubble.MOD_ID, "retro/magenta", RETRO_SHADERS);
	public static final Identifier PINK_RETRO = register(Mubble.MOD_ID, "retro/pink", RETRO_SHADERS);

	private static Identifier register(String namespace, String name) {
		Identifier shaderRL = new Identifier(namespace, "shaders/post/" + name + ".json");
		SHADERS.add(shaderRL);
		return shaderRL;
	}

	private static Identifier register(String namespace, String name, List<Identifier> aditionalList) {
		Identifier shaderRL = new Identifier(namespace, "shaders/post/" + name + ".json");
		SHADERS.add(shaderRL);
		aditionalList.add(shaderRL);
		return shaderRL;
	}
}