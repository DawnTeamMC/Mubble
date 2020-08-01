package com.hugman.mubble.util.trade_offer;

import com.hugman.mubble.init.MubbleBlockPack;
import com.hugman.mubble.init.MubbleCostumePack;
import com.hugman.mubble.init.MubbleItemPack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

import java.util.Random;

public class ToadTradeOffers {
	public static final Item YELLOW_COIN = MubbleItemPack.YELLOW_COIN;
	public static final Item RED_COIN = MubbleItemPack.RED_COIN;
	public static final Item BLUE_COIN = MubbleItemPack.BLUE_COIN;

	public static final TradeOffers.Factory[] COIN_TRADES = new TradeOffers.Factory[]{
			new ToadTradeOffers.SellItemFactory(YELLOW_COIN, 6, MubbleItemPack.RED_COIN, 1, 32),
			new ToadTradeOffers.SellItemFactory(RED_COIN, 2, MubbleItemPack.BLUE_COIN, 1, 16),
			new ToadTradeOffers.SellItemFactory(RED_COIN, 1, MubbleItemPack.YELLOW_COIN, 5, 32),
			new ToadTradeOffers.SellItemFactory(BLUE_COIN, 1, MubbleItemPack.RED_COIN, 2, 32),
			new ToadTradeOffers.SellItemFactory(BLUE_COIN, 1, MubbleItemPack.YELLOW_COIN, 9, 24)
	};
	public static final TradeOffers.Factory[] PRIMARY_COSTUMES_TRADES = new TradeOffers.Factory[]{
			new ToadTradeOffers.SellCostumeFactory(BLUE_COIN, 2, MubbleCostumePack.CAPPY),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 5, MubbleCostumePack.LUIGI_CAP),
			new ToadTradeOffers.SellCostumeFactory(YELLOW_COIN, 5, MubbleCostumePack.WARIO_CAP),
			new ToadTradeOffers.SellCostumeFactory(YELLOW_COIN, 5, MubbleCostumePack.WALUIGI_CAP),
			new ToadTradeOffers.SellCostumeFactory(YELLOW_COIN, 5, MubbleCostumePack.GOOIGI_CAP),
			new ToadTradeOffers.SellCostumeFactory(YELLOW_COIN, 5, MubbleCostumePack.MARIO_WEDDING_HAT),
			new ToadTradeOffers.SellCostumeFactory(YELLOW_COIN, 10, MubbleCostumePack.BOO_HAT, 4),
			new ToadTradeOffers.SellCostumeFactory(YELLOW_COIN, 5, MubbleCostumePack.BROQUE_MONSIEUR_HEAD),
			new ToadTradeOffers.SellCostumeFactory(YELLOW_COIN, 5, MubbleCostumePack.MAYRO_CAP)
	};
	public static final TradeOffers.Factory[] SECONDARY_COSTUMES_TRADES = new TradeOffers.Factory[]{
			new ToadTradeOffers.SellCostumeFactory(BLUE_COIN, 12, MubbleCostumePack.VANISH_CAP, 3),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 4, MubbleCostumePack.GOLD_MARIO_CAP),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 4, MubbleCostumePack.SILVER_LUIGI_CAP),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 5, MubbleCostumePack.PRINCESS_PEACH_CROWN),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 5, MubbleCostumePack.PRINCESS_DAISY_CROWN),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 5, MubbleCostumePack.ROSALINA_CROWN),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 5, MubbleCostumePack.PINK_GOLD_PEACH_CROWN),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 4, MubbleCostumePack.SUPER_CROWN),
			new ToadTradeOffers.SellCostumeFactory(RED_COIN, 4, MubbleCostumePack.KORETATO_BLOCK)
	};
	public static final TradeOffers.Factory[] BLOCK_TRADES = new TradeOffers.Factory[]{
			new ToadTradeOffers.SellItemFactory(BLUE_COIN, 1, MubbleBlockPack.NSMBU_QUESTION_BLOCK.asItem(), 3, 10)
	};

	static class SellItemFactory implements TradeOffers.Factory {
		private final Item priceItem;
		private final int priceAmount;
		private final float priceMultiplier;
		private final Item sellItem;
		private final int sellAmount;
		private final int maxUses;
		private final int experience;

		public SellItemFactory(Item priceItem, int priceAmount, float priceMultiplier, Item sellItem, int sellAmount, int maxUses, int experience) {
			this.priceItem = priceItem;
			this.priceAmount = priceAmount;
			this.priceMultiplier = priceMultiplier;
			this.sellItem = sellItem;
			this.sellAmount = sellAmount;
			this.maxUses = maxUses;
			this.experience = experience;
		}

		public SellItemFactory(Item priceItem, int priceAmount, Item sellItem, int sellAmount, int maxUses, int experience) {
			this(priceItem, priceAmount, 0.05f, sellItem, sellAmount, maxUses, experience);
		}

		public SellItemFactory(Item priceItem, int priceAmount, Item sellItem, int sellAmount, int maxUses) {
			this(priceItem, priceAmount, sellItem, sellAmount, maxUses, 1);
		}

		@Override
		public TradeOffer create(Entity entity, Random random) {
			return new TradeOffer(new ItemStack(this.priceItem, this.priceAmount), new ItemStack(this.sellItem, this.sellAmount), this.maxUses, this.experience, this.priceMultiplier);
		}
	}

	static class SellCostumeFactory implements TradeOffers.Factory {
		private final Item priceItem;
		private final int priceAmount;
		private final float priceMultiplier;
		private final Item sellItem;
		private final int maxUses;
		private final int experience;

		public SellCostumeFactory(Item priceItem, int priceAmount, float priceMultiplier, Item sellItem, int maxUses, int experience) {
			this.priceItem = priceItem;
			this.priceAmount = priceAmount;
			this.priceMultiplier = priceMultiplier;
			this.sellItem = sellItem;
			this.maxUses = maxUses;
			this.experience = experience;
		}

		public SellCostumeFactory(Item priceItem, int priceAmount, Item sellItem, int maxUses, int experience) {
			this(priceItem, priceAmount, 0.05F, sellItem, maxUses, experience);
		}

		public SellCostumeFactory(Item priceItem, int priceAmount, Item sellItem, int maxUses) {
			this(priceItem, priceAmount, sellItem, maxUses, 1);
		}

		public SellCostumeFactory(Item priceItem, int priceAmount, Item sellItem) {
			this(priceItem, priceAmount, sellItem, 1);
		}

		@Override
		public TradeOffer create(Entity entity, Random random) {
			return new TradeOffer(new ItemStack(this.priceItem, this.priceAmount), new ItemStack(this.sellItem, 1), this.maxUses, this.experience, this.priceMultiplier);
		}
	}
}
