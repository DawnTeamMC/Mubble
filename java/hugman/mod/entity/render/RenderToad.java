package hugman.mod.entity.render;

import java.util.Map;

import com.google.common.collect.Maps;

import hugman.mod.entity.EntityToad;
import hugman.mod.entity.model.ModelToad;
import hugman.mod.util.Reference;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderToad extends RenderLiving<EntityToad>
{
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.<String, ResourceLocation>newHashMap();
    private static final ResourceLocation BLUE_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/blue.png");
    private static final ResourceLocation LIGHT_BLUE_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/light_blue.png");
    private static final ResourceLocation CYAN_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/cyan.png");
    private static final ResourceLocation GREEN_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/green.png");
    private static final ResourceLocation LIME_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/lime.png");
    private static final ResourceLocation YELLOW_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/yellow.png");
    private static final ResourceLocation ORANGE_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/orange.png");
    private static final ResourceLocation RED_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/red.png");
    private static final ResourceLocation PINK_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/pink.png");
    private static final ResourceLocation MAGENTA_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/magenta.png");
    private static final ResourceLocation PURPLE_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/purple.png");
    private static final ResourceLocation BROWN_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/brown.png");
    private static final ResourceLocation WHITE_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/white.png");
    private static final ResourceLocation LIGHT_GRAY_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/light_gray.png");
    private static final ResourceLocation GRAY_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/gray.png");
    private static final ResourceLocation BLACK_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/species/black.png");
    private static final ResourceLocation CAPTAIN_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/brigade/captain.png");
    private static final ResourceLocation HINT_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/brigade/hint.png");
    private static final ResourceLocation BANKTOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/brigade/bank.png");
    private static final ResourceLocation MAILTOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/brigade/mail.png");
    private static final ResourceLocation YELLOW_BTOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/brigade/yellow.png");
    private static final ResourceLocation PARTY_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/party.png");
    private static final ResourceLocation KISEKAE_TOAD_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad/kisekae.png");
	
	public RenderToad(RenderManager manager) 
	{
		super(manager, new ModelToad(), 0.5F);
	}

	@Override
    protected ResourceLocation getEntityTexture(EntityToad entity)
    {
		int texture = entity.getColor();
		for (int row = 0; row < 2; row++)
        {
            if(texture == 0) return BLUE_TOAD_TEXTURES;
            else if(texture == 1) return LIGHT_BLUE_TOAD_TEXTURES;
            else if(texture == 2) return CYAN_TOAD_TEXTURES;
            else if(texture == 3) return GREEN_TOAD_TEXTURES;
            else if(texture == 4) return LIME_TOAD_TEXTURES;
            else if(texture == 5) return YELLOW_TOAD_TEXTURES;
            else if(texture == 6) return ORANGE_TOAD_TEXTURES;
            else if(texture == 7) return RED_TOAD_TEXTURES;
            else if(texture == 8) return PINK_TOAD_TEXTURES;
            else if(texture == 9) return MAGENTA_TOAD_TEXTURES;
            else if(texture == 10) return PURPLE_TOAD_TEXTURES;
            else if(texture == 11) return BROWN_TOAD_TEXTURES;
            else if(texture == 12) return WHITE_TOAD_TEXTURES;
            else if(texture == 13) return LIGHT_GRAY_TOAD_TEXTURES;
            else if(texture == 14) return GRAY_TOAD_TEXTURES;
            else if(texture == 15) return BLACK_TOAD_TEXTURES;
            else if(texture == 100) return CAPTAIN_TOAD_TEXTURES;
            else if(texture == 101) return HINT_TOAD_TEXTURES;
            else if(texture == 102) return BANKTOAD_TEXTURES;
            else if(texture == 103) return YELLOW_BTOAD_TEXTURES;
            else if(texture == 104) return MAILTOAD_TEXTURES;
            else if(texture == 105) return PARTY_TOAD_TEXTURES;
            else if(texture == 106) return KISEKAE_TOAD_TEXTURES;
            else
            {
            	texture = texture - 5;
            	row = 0;
            }
        }
		return RED_TOAD_TEXTURES;
    }

	@Override
	protected void applyRotations(EntityToad entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
