package hugman.mubble.init.data;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class MubbleFoods
{
	public static final int crepeHunger = 3;
	public static final float crepeSaturation = 0.5f;
	
	public static final FoodComponent TOMATO = (new FoodComponent.Builder()).hunger(3).saturationModifier(0.6F).build();
	public static final FoodComponent SALAD = (new FoodComponent.Builder()).hunger(2).saturationModifier(0.7F).build();
    public static final FoodComponent BLUEBERRIES = (new FoodComponent.Builder()).hunger(2).saturationModifier(0.1F).build();
	public static final FoodComponent CHEESE = (new FoodComponent.Builder()).hunger(2).saturationModifier(0.4F).build();
    public static final FoodComponent BANANA = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build();
    public static final FoodComponent APRICOT = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build();
    public static final FoodComponent MANGO = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build();
    public static final FoodComponent CARAMEL_CUBE = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.2F).build();
    public static final FoodComponent BAGUETTE = (new FoodComponent.Builder()).hunger(7).saturationModifier(0.7F).build();
    public static final FoodComponent BURGER = (new FoodComponent.Builder()).hunger(9).saturationModifier(0.9F).build();
    public static final FoodComponent CREPE = (new FoodComponent.Builder()).hunger(crepeHunger).saturationModifier(crepeSaturation).build();
    public static final FoodComponent SUGAR_CREPE = (new FoodComponent.Builder()).hunger(crepeHunger + 2).saturationModifier(crepeSaturation + 0.2f).build();
    public static final FoodComponent CHOCOLATE_CREPE = (new FoodComponent.Builder()).hunger(crepeHunger + 4).saturationModifier(crepeSaturation + 0.4f).build();
    public static final FoodComponent CARAMEL_CREPE = (new FoodComponent.Builder()).hunger(crepeHunger + 4).saturationModifier(crepeSaturation + 0.3f).build();
    public static final FoodComponent HONEY_CREPE = (new FoodComponent.Builder()).hunger(crepeHunger + 2).saturationModifier(crepeSaturation + 0.2f).build();
    public static final FoodComponent SWEET_BERRY_CREPE = (new FoodComponent.Builder()).hunger(crepeHunger + 2).saturationModifier(crepeSaturation + 0.1f).build();
    public static final FoodComponent BLUEBERRY_CREPE = (new FoodComponent.Builder()).hunger(crepeHunger + 2).saturationModifier(crepeSaturation + 0.1f).build();
    public static final FoodComponent CANDY_CANE = (new FoodComponent.Builder()).hunger(3).saturationModifier(0.6F).build();
    public static final FoodComponent DUCK = (new FoodComponent.Builder()).hunger(2).saturation(0.3F).effect(() -> new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).meat().build();
    public static final FoodComponent COOKED_DUCK = (new FoodComponent.Builder()).hunger(6).saturation(0.6F).meat().build();

    public static final FoodComponent SUPER_MUSHROOM = (new FoodComponent.Builder()).hunger(3).saturationModifier(0.5F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 550, 1), 1.0F).build();
    public static final FoodComponent PEACH = (new FoodComponent.Builder()).hunger(3).saturationModifier(0.5F).build();
    public static final FoodComponent SUPER_STAR = (new FoodComponent.Builder()).hunger(3).saturationModifier(1.5F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0), 1.0F).build();
}