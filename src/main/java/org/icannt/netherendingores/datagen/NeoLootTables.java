package org.icannt.netherendingores.datagen;

import java.util.Set;

import org.icannt.netherendingores.Registration;
import org.icannt.netherendingores.common.block.NeoOreBlocks;
import org.icannt.netherendingores.common.loot.TagNotEmptyCondition;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class NeoLootTables extends BlockLootSubProvider {

    public NeoLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            Block block = ore.getBlock();

            if (ore.isVanilla()) {
                add(block, createSilkTouchDispatchTable(block, createDrop(ore, ore.getVanillaLootItem())));
            } else {
                // Try the preferred material tag, then compatibility fallbacks
                // such as dusts for sulfur/niter, ingots for metals/tungsten,
                // peridot for green sapphire, and yellorium for yellorite.
                LootItemCondition.Builder anyTagAvailable = AnyOfCondition.anyOf(
                        ore.getOreLootItemTags().stream()
                                .map(TagNotEmptyCondition::new)
                                .toArray(LootItemCondition.Builder[]::new)
                );
                LootPoolEntryContainer.Builder<?> tagDrops = AlternativesEntry.alternatives(
                        ore.getOreLootItemTags(),
                        tag -> applyExplosionDecay(block, TagEntry.expandTag(tag)
                                .when(() -> new TagNotEmptyCondition(tag))
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(ApplyBonusCount.addUniformBonusCount(fortune(), 1)))
                ).when(anyTagAvailable);
                // Fall back to the ore block when no mod supplies the material.
                add(block, createSilkTouchDispatchTable(block, AlternativesEntry.alternatives(
                        tagDrops,
                        applyExplosionDecay(block, LootItem.lootTableItem(block))
                )));
            }
        }

        dropSelf(Registration.EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK.get());
    }

    private LootPoolEntryContainer.Builder<?> createDrop(NeoOreBlocks ore, Item item) {
        return applyExplosionDecay(ore.getBlock(), LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                .apply(ApplyBonusCount.addUniformBonusCount(fortune(), 1)));
    }

    private Holder<Enchantment> fortune() {
        return this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registration.getBlocks().toList();
    }

}
