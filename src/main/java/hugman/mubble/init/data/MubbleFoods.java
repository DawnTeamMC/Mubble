package hugman.mubble.init.data;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class MubbleFoods
{
	public static final int crepeHunger = 3;
	public static final float crepeSaturation = 0.5f;
	
	public static final Food TOMATO = (new Food.Builder()).hunger(3).saturation(0.6F).build();
	public static final Food SALAD = (new Food.Builder()).hunger(2).saturation(0.7F).build();
    public static final Food BLUEBERRIES = (new Food.Builder()).hunger(2).saturation(0.1F).build();
	public static final Food CHEESE = (new Food.Builder()).hunger(2).saturation(0.4F).build();
    public static final Food BANANA = (new Food.Builder()).hunger(4).saturation(0.3F).build();
    public static final Food APRICOT = (new Food.Builder()).hunger(4).saturation(0.3F).build();
    public static final Food MANGO = (new Food.Builder()).hunger(4).saturation(0.3F).build();
    public static final Food CARAMEL_CUBE = (new Food.Builder()).hunger(4).saturation(0.2F).build();
    public static final Food BAGUETTE = (new Food.Builder()).hunger(7).saturation(0.7F).build();
    public static final Food BURGER = (new Food.Builder()).hunger(9).saturation(0.9F).build();
    public static final Food CREPE = (new Food.Builder()).hunger(crepeHunger).saturation(crepeSaturation).build();
    public static final Food SUGAR_CREPE = (new Food.Builder()).hunger(crepeHunger + 2).saturation(crepeSaturation + 0.2f).build();
    public static final Food CHOCOLATE_CREPE = (new Food.Builder()).hunger(crepeHunger + 4).saturation(crepeSaturation + 0.4f).build();
    public static final Food CARAMEL_CREPE = (new Food.Builder()).hunger(crepeHunger + 4).saturation(crepeSaturation + 0.3f).build();
    public static final Food HONEY_CREPE = (new Food.Builder()).hunger(crepeHunger + 2).saturation(crepeSaturation + 0.2f).build();
    public static final Food SWEET_BERRY_CREPE = (new Food.Builder()).hunger(crepeHunger + 2).saturation(crepeSaturation + 0.1f).build();
    public static final Food BLUEBERRY_CREPE = (new Food.Builder()).hunger(crepeHunger + 2).saturation(crepeSaturation + 0.1f).build();
    public static final Food CANDY_CANE = (new Food.Builder()).hunger(3).saturation(0.6F).build();

    public static final Food SUPER_MUSHROOM = (new Food.Builder()).hunger(3).saturation(0.5F).setAlwaysEdible().effect(() -> new EffectInstance(Effects.JUMP_BOOST, 550, 1), 1.0F).build();
    public static final Food PEACH = (new Food.Builder()).hunger(3).saturation(0.5F).build();
    public static final Food SUPER_STAR = (new Food.Builder()).hunger(3).saturation(1.5F).setAlwaysEdible().effect(() -> new EffectInstance(Effects.SPEED, 600, 1), 1.0F).effect(() -> new EffectInstance(Effects.STRENGTH, 600, 0), 1.0F).effect(() -> new EffectInstance(Effects.REGENERATION, 600, 1), 1.0F).effect(() -> new EffectInstance(Effects.RESISTANCE, 600, 1), 1.0F).effect(() -> new EffectInstance(Effects.NIGHT_VISION, 600, 0), 1.0F).build();
}