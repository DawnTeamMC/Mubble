package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.ToadEntity;
import hugman.mubble.objects.entity.render.model.ToadEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ToadEntityRenderer extends MobEntityRenderer<ToadEntity, ToadEntityModel<ToadEntity>>
{
	private static final Identifier BLUE_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/blue.png");
	private static final Identifier LIGHT_BLUE_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/light_blue.png");
	private static final Identifier CYAN_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/cyan.png");
	private static final Identifier GREEN_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/green.png");
	private static final Identifier LIME_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/lime.png");
	private static final Identifier YELLOW_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/yellow.png");
	private static final Identifier ORANGE_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/orange.png");
	private static final Identifier RED_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/red.png");
	private static final Identifier PINK_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/pink.png");
	private static final Identifier MAGENTA_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/magenta.png");
	private static final Identifier PURPLE_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/purple.png");
	private static final Identifier BROWN_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/brown.png");
	private static final Identifier WHITE_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/white.png");
	private static final Identifier LIGHT_GRAY_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/light_gray.png");
	private static final Identifier GRAY_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/gray.png");
	private static final Identifier BLACK_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/species/black.png");
	private static final Identifier CAPTAIN_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/brigade/captain.png");
	private static final Identifier HINT_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/brigade/hint.png");
	private static final Identifier BANKTOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/brigade/bank.png");
	private static final Identifier MAILTOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/brigade/mail.png");
	private static final Identifier YELLOW_BTOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/brigade/yellow.png");
	private static final Identifier PARTY_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/party.png");
	private static final Identifier KISEKAE_TOAD_TEXTURES = new Identifier(Mubble.MOD_PREFIX + "textures/entity/toad/kisekae.png");

	public ToadEntityRenderer(EntityRenderDispatcher dispatcher)
	{
		super(dispatcher, new ToadEntityModel<>(), 0.5F);
	}

	@Override
	public Identifier getTexture(ToadEntity entity)
	{
		switch (entity.getVariant())
		{
			case 0:
				return WHITE_TOAD_TEXTURES;
			case 1:
				return LIGHT_GRAY_TOAD_TEXTURES;
			case 2:
				return GRAY_TOAD_TEXTURES;
			case 3:
				return BLACK_TOAD_TEXTURES;
			case 4:
				return BROWN_TOAD_TEXTURES;
			case 5:
			default:
				return RED_TOAD_TEXTURES;
			case 6:
				return ORANGE_TOAD_TEXTURES;
			case 7:
				return YELLOW_TOAD_TEXTURES;
			case 8:
				return LIME_TOAD_TEXTURES;
			case 9:
				return GREEN_TOAD_TEXTURES;
			case 10:
				return CYAN_TOAD_TEXTURES;
			case 11:
				return LIGHT_BLUE_TOAD_TEXTURES;
			case 12:
				return BLUE_TOAD_TEXTURES;
			case 13:
				return PURPLE_TOAD_TEXTURES;
			case 14:
				return MAGENTA_TOAD_TEXTURES;
			case 15:
				return PINK_TOAD_TEXTURES;
			case 100:
				return CAPTAIN_TOAD_TEXTURES;
			case 101:
				return HINT_TOAD_TEXTURES;
			case 102:
				return BANKTOAD_TEXTURES;
			case 103:
				return YELLOW_BTOAD_TEXTURES;
			case 104:
				return MAILTOAD_TEXTURES;
			case 105:
				return PARTY_TOAD_TEXTURES;
			case 106:
				return KISEKAE_TOAD_TEXTURES;
		}
	}
}