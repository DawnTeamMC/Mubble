package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.block.block_state_property.Princess;
import hugman.mubble.objects.costume.BlockCostume;
import hugman.mubble.objects.costume.CappyCostume;
import hugman.mubble.objects.costume.ChristmasHatCostume;
import hugman.mubble.objects.costume.CrownCostume;
import hugman.mubble.objects.costume.GooigiCapCostume;
import hugman.mubble.objects.costume.GuardianMaskCostume;
import hugman.mubble.objects.costume.HeadCostume;
import hugman.mubble.objects.costume.MayroCapCostume;
import hugman.mubble.objects.costume.VanishCapCostume;
import hugman.mubble.objects.costume.WingCapCostume;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;

public class MubbleCostumes
{
    /* All Content Bag */
    public static final List<Item> COSTUMES = new ArrayList<Item>();

    /* Templates */
    protected static final Item.Properties pSimple = new Item.Properties().group(MubbleTabs.COSTUMES).maxStackSize(1);
    protected static final Item.Properties pBlockDecorations = new Item.Properties().group(ItemGroup.DECORATIONS);
    
    /* MINECRAFT */
    public static final Item HEADPHONES = register("headphones", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
    
    public static final Item WHITE_VIRTUAL_GOOGLES = register("white_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.WHITE_RETRO));
    public static final Item LIGHT_GRAY_VIRTUAL_GOOGLES = register("light_gray_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIGHT_GRAY_RETRO));
    public static final Item GRAY_VIRTUAL_GOOGLES = register("gray_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.GRAY_RETRO));
    public static final Item BLACK_VIRTUAL_GOOGLES = register("black_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BLACK_RETRO));
    public static final Item BROWN_VIRTUAL_GOOGLES = register("brown_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BROWN_RETRO));
    public static final Item RED_VIRTUAL_GOOGLES = register("red_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.RED_RETRO));
    public static final Item ORANGE_VIRTUAL_GOOGLES = register("orange_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.ORANGE_RETRO));
    public static final Item YELLOW_VIRTUAL_GOOGLES = register("yellow_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.YELLOW_RETRO));
    public static final Item LIME_VIRTUAL_GOOGLES = register("lime_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIME_RETRO));
    public static final Item GREEN_VIRTUAL_GOOGLES = register("green_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.GREEN_RETRO));
    public static final Item CYAN_VIRTUAL_GOOGLES = register("cyan_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.CYAN_RETRO));
    public static final Item LIGHT_BLUE_VIRTUAL_GOOGLES = register("light_blue_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIGHT_BLUE_RETRO));
    public static final Item BLUE_VIRTUAL_GOOGLES = register("blue_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BLUE_RETRO));
    public static final Item PURPLE_VIRTUAL_GOOGLES = register("purple_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.PURPLE_RETRO));
    public static final Item MAGENTA_VIRTUAL_GOOGLES = register("magenta_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.MAGENTA_RETRO));
    public static final Item PINK_VIRTUAL_GOOGLES = register("pink_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.PINK_RETRO));
    
    public static final Item CHRISTMAS_HAT = register("christmas_hat", new ChristmasHatCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

    /* SUPER MARIO */
    public static final Item CAPPY = register("cappy", new CappyCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
    public static final Item LUIGI_CAP = register("luigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new EffectInstance(Effects.JUMP_BOOST, 5, 0), new EffectInstance(Effects.SPEED, 5, 0)));
    public static final Item WARIO_CAP = register("wario_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new EffectInstance(Effects.SLOWNESS, 5, 0), new EffectInstance(Effects.STRENGTH, 5, 0)));
    public static final Item WALUIGI_CAP = register("waluigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new EffectInstance(Effects.JUMP_BOOST, 5, 1)));
    public static final Item VANISH_CAP = register("vanish_cap", new VanishCapCostume(pSimple));
    public static final Item WING_CAP = register("wing_cap", new WingCapCostume(pSimple.defaultMaxDamage(600)));
    public static final Item GOOIGI_CAP = register("gooigi_cap", new GooigiCapCostume(pSimple, SoundEvents.BLOCK_SLIME_BLOCK_PLACE));
    public static final Item GOLD_MARIO_CAP = register("gold_mario_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_GOLD));
    public static final Item SILVER_LUIGI_CAP = register("silver_luigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
    public static final Item PRINCESS_PEACH_CROWN = register("princess_peach_crown", new CrownCostume(pSimple, Princess.PEACH));
    public static final Item PRINCESS_DAISY_CROWN = register("princess_daisy_crown", new CrownCostume(pSimple, Princess.DAISY));
    public static final Item ROSALINA_CROWN = register("rosalina_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
    public static final Item PINK_GOLD_PEACH_CROWN = register("pink_gold_peach_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
    public static final Item SUPER_CROWN = register("super_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
    public static final Item MARIO_WEDDING_HAT = register("mario_wedding_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
    public static final Item BOO_HAT = register("boo_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
    public static final Item BROQUE_MONSIEUR_HEAD = register("broque_monsieur_head", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
    
    /* KIRBY */
    public static final Item BANDANA = register("bandana", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
    
    /* POKEMON */
    public static final Item PIKACHU_EARS = register("pikachu_ears", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
    public static final Item SNORLAX_HAT = register("snorlax_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

    /* PROFESSOR LAYTON */
    public static final Item TOP_HAT = register("top_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
    
    /* UNDERTALE / DELTARUNE */
    public static final Item RALSEI_HAT = register("ralsei_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
    public static final Item KING_ROUND_MASK = register("king_round_mask", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
    
    /* A HAT IN TIME */
    public static final Item KID_HAT = register("kid_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

    /* PUYO PUYO */
    public static final Item RED_PUYO = register("red_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.RED_PUYO));
    public static final Item YELLOW_PUYO = register("yellow_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.YELLOW_PUYO));
    public static final Item GREEN_PUYO = register("green_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.GREEN_PUYO));
    public static final Item TURQUOISE_PUYO = register("turquoise_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.TURQUOISE_PUYO));
    public static final Item BLUE_PUYO = register("blue_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.BLUE_PUYO));
    public static final Item PURPLE_PUYO = register("purple_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.PURPLE_PUYO));
    public static final Item GRAY_PUYO = register("gray_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.GRAY_PUYO));
    public static final Item GARBAGE_PUYO = register("garbage_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.GARBAGE_PUYO));
    public static final Item POINT_PUYO = register("point_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.POINT_PUYO));
    public static final Item HARD_PUYO = register("hard_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.HARD_PUYO));
    public static final Item IRON_PUYO = register("iron_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_METAL_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.IRON_PUYO));
    
    /* BALDI'S BASICS IN EDUCATION AND LEARNING */
    public static final Item BALDI_HEAD = register("baldi_head", new HeadCostume(pSimple, SoundEvents.ENTITY_PARROT_IMITATE_VEX));

    /* PETSCOP */
    public static final Item GUARDIAN_MASK = register("guardian_mask", new GuardianMaskCostume(pSimple));

    /* YOUTUBE */
    public static final Item MAYRO_CAP = register("mayro_cap", new MayroCapCostume(pSimple));
    public static final Item KORETATO_BLOCK = register("koretato_block", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SNOW_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.KORETATO_BLOCK));
    public static final Item NOTEBLOCK_HEAD = register("noteblock_head", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
    
    private static Item register(String name, Item item)
    {
        Item fCostume = item.setRegistryName(Mubble.MOD_ID, name);
        COSTUMES.add(fCostume);
        return fCostume;
    }
}
