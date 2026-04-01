package com.zitrone.lemonthings.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddItemLootModifier extends LootModifier {

    private final Item item;
    private final int minCount;
    private final int maxCount;
    private final float chance;

    public AddItemLootModifier(LootItemCondition[] conditions, Item item, int minCount, int maxCount, float chance) {
        super(conditions);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.chance = chance;
    }

    public static final MapCodec<AddItemLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(
                    inst.group(
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter((AddItemLootModifier m) -> m.item),
                            Codec.INT.fieldOf("min_count").forGetter((AddItemLootModifier m) -> m.minCount),
                            Codec.INT.fieldOf("max_count").forGetter((AddItemLootModifier m) -> m.maxCount),
                            Codec.FLOAT.fieldOf("chance").forGetter((AddItemLootModifier m) -> m.chance)
                    )
            ).apply(inst, AddItemLootModifier::new)
    );

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        float random = context.getRandom().nextFloat();
        if (random <= chance) {
            int count = minCount == maxCount ? minCount :
                    minCount + context.getRandom().nextInt(maxCount - minCount + 1);
            generatedLoot.add(new ItemStack(item, count));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}