package com.zitrone.lemonthings;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

    public static class Common {
        public final ModConfigSpec.BooleanValue enableWitchBag;
        public final ModConfigSpec.BooleanValue enablePiglinBruteBag;
        public final ModConfigSpec.BooleanValue enablePillagerBag;
        public final ModConfigSpec.BooleanValue enableWanderingTraderBag;
        public final ModConfigSpec.BooleanValue enableRavagerLeather;

        public final ModConfigSpec.DoubleValue witchBagChance;
        public final ModConfigSpec.DoubleValue piglinBruteBagChance;
        public final ModConfigSpec.DoubleValue pillagerBagChance;
        public final ModConfigSpec.DoubleValue wanderingTraderBagChance;
        public final ModConfigSpec.DoubleValue ravagerLeatherChance;

        public final ModConfigSpec.BooleanValue enableWitheredBoneDrop;
        public final ModConfigSpec.DoubleValue witheredBoneDropChance;

        public final ModConfigSpec.BooleanValue enableFeatherPaperCraft;
        public final ModConfigSpec.BooleanValue enableArrowFromFeatherPaper;

        public final ModConfigSpec.BooleanValue enableAmethystSwordEnchant;
        public final ModConfigSpec.BooleanValue enableAmethystSwordRepair;

        public final ModConfigSpec.BooleanValue enableSimplestBowRepair;
        public final ModConfigSpec.BooleanValue enableBoneBowRepair;

        public final ModConfigSpec.BooleanValue enableWitheredBoneMeal;

        public final ModConfigSpec.BooleanValue enableMendingInWanderingTrader;
        public final ModConfigSpec.DoubleValue mendingPrice;

        public final ModConfigSpec.BooleanValue replaceEnchantedBooksWithCandles;
        public final ModConfigSpec.BooleanValue addWanderingTraderTrades;

        Common(ModConfigSpec.Builder builder) {
            builder.comment("LemonThings Mod Configuration").push("general");

            builder.comment("Bag Drops from Mobs").push("bag_drops");

            enableWitchBag = builder
                    .comment("Enable Witch Bag drop from witches")
                    .define("enableWitchBag", true);

            witchBagChance = builder
                    .comment("Chance for Witch Bag to drop (0.0 - 1.0)")
                    .defineInRange("witchBagChance", 0.5, 0.0, 1.0);

            enablePiglinBruteBag = builder
                    .comment("Enable Piglin Bag drop from piglin brutes")
                    .define("enablePiglinBruteBag", true);

            piglinBruteBagChance = builder
                    .comment("Chance for Piglin Bag to drop (0.0 - 1.0)")
                    .defineInRange("piglinBruteBagChance", 0.4, 0.0, 1.0);

            enablePillagerBag = builder
                    .comment("Enable Pillager Bag drop from pillagers")
                    .define("enablePillagerBag", true);

            pillagerBagChance = builder
                    .comment("Chance for Pillager Bag to drop (0.0 - 1.0)")
                    .defineInRange("pillagerBagChance", 0.2, 0.0, 1.0);

            enableWanderingTraderBag = builder
                    .comment("Enable Wandering Trader Bag drop from wandering traders")
                    .define("enableWanderingTraderBag", true);

            wanderingTraderBagChance = builder
                    .comment("Chance for Wandering Trader Bag to drop (0.0 - 1.0)")
                    .defineInRange("wanderingTraderBagChance", 1.0, 0.0, 1.0);

            builder.pop();

            builder.comment("Withered Bone").push("withered_bone");

            enableWitheredBoneDrop = builder
                    .comment("Enable withered bone drop from wither skeletons")
                    .define("enableWitheredBoneDrop", true);

            witheredBoneDropChance = builder
                    .comment("Chance for withered bone to drop (0.0 - 1.0)")
                    .defineInRange("witheredBoneDropChance", 0.5, 0.0, 1.0);

            builder.pop();

            builder.comment("Ravager Leather").push("ravager_leather");

            enableRavagerLeather = builder
                    .comment("Enable ravager leather drop from ravagers")
                    .define("enableRavagerLeather", true);

            ravagerLeatherChance = builder
                    .comment("Chance for ravager leather to drop (0.0 - 1.0)")
                    .defineInRange("ravagerLeatherChance", 0.5, 0.0, 1.0);

            builder.pop();

            builder.comment("Feather Paper").push("feather_paper");

            enableFeatherPaperCraft = builder
                    .comment("Enable feather paper crafting (right-click paper on fletching table)")
                    .define("enableFeatherPaperCraft", true);

            enableArrowFromFeatherPaper = builder
                    .comment("Enable arrow recipe from feather paper")
                    .define("enableArrowFromFeatherPaper", true);

            builder.pop();

            builder.comment("Amethyst Sword").push("amethyst_sword");

            enableAmethystSwordEnchant = builder
                    .comment("Enable amethyst sword enchanting on budding amethyst")
                    .define("enableAmethystSwordEnchant", true);

            enableAmethystSwordRepair = builder
                    .comment("Enable amethyst sword repair with amethyst blocks")
                    .define("enableAmethystSwordRepair", true);

            builder.pop();

            builder.comment("Bows").push("bows");

            enableSimplestBowRepair = builder
                    .comment("Enable simplest bow repair with sticks")
                    .define("enableSimplestBowRepair", true);

            enableBoneBowRepair = builder
                    .comment("Enable bone bow repair with bones or withered bones")
                    .define("enableBoneBowRepair", true);

            builder.pop();

            builder.comment("Withered Bone Meal").push("withered_bone_meal");

            enableWitheredBoneMeal = builder
                    .comment("Enable withered bone meal to create wither roses on soul sand/soil")
                    .define("enableWitheredBoneMeal", true);

            builder.pop();

            builder.comment("Wandering Trader").push("wandering_trader");

            enableMendingInWanderingTrader = builder
                    .comment("Enable mending book in wandering trader trades")
                    .define("enableMendingInWanderingTrader", true);

            mendingPrice = builder
                    .comment("Price in emeralds for mending book")
                    .defineInRange("mendingPrice", 16.0, 1.0, 64.0);

            addWanderingTraderTrades = builder
                    .comment("Enable additional wandering trader trades (ender pearls, blaze rods, etc.)")
                    .define("addWanderingTraderTrades", true);

            replaceEnchantedBooksWithCandles = builder
                    .comment("Replace enchanted books with candles in librarian trades")
                    .define("replaceEnchantedBooksWithCandles", true);

            builder.pop();

            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }
}