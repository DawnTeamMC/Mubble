package hugman.mod.objects.potion_effect;

import hugman.mod.Mubble;
import hugman.mod.init.MubblePotionEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EffectSimple extends Potion
{	
	private final ResourceLocation iconTexture;
	
	public EffectSimple(String name, boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);
		setRegistryName(Mubble.MOD_ID, name);
		iconTexture = new ResourceLocation(Mubble.MOD_ID, "textures/gui/effects/" + name + ".png");
		MubblePotionEffects.register(this);
	}
	
	@Override
	protected Potion setIconIndex(int x, int y)
	{
		return super.setIconIndex(0, 0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z)
	{
		Minecraft.getInstance().getTextureManager().bindTexture(iconTexture);
		Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha)
	{
		Minecraft.getInstance().getTextureManager().bindTexture(iconTexture);
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}
}