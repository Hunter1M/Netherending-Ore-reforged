package org.icannt.netherendingores.worldgen;

import java.util.Optional;

import org.icannt.netherendingores.NetherendingOres;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.neoforged.fml.ModList;

/**
 * An ore feature that only places its wrapped ore when the material it belongs
 * to is provided by an installed mod or by an existing common tag.
 */
public class ConditionalOreFeature extends Feature<ConditionalOreConfiguration> {

    private static final OreFeature ORE_FEATURE = new OreFeature(OreConfiguration.CODEC);

    public ConditionalOreFeature() {
        super(ConditionalOreConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<ConditionalOreConfiguration> context) {
        ConditionalOreConfiguration config = context.config();
        if (!shouldGenerate(config)) {
            return false;
        }

        FeaturePlaceContext<OreConfiguration> delegate = new FeaturePlaceContext<>(
                context.topFeature(),
                context.level(),
                context.chunkGenerator(),
                context.random(),
                context.origin(),
                config.ore()
        );
        return ORE_FEATURE.place(delegate);
    }

    private static boolean shouldGenerate(ConditionalOreConfiguration config) {
        ModList modList = ModList.get();
        if (modList != null) {
            for (String modId : config.mods()) {
                if (modList.isLoaded(modId)) {
                    return true;
                }
            }
        }

        for (ResourceLocation tagId : config.tags()) {
            if (hasExternalEntry(tagId)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasExternalEntry(ResourceLocation tagId) {
        TagKey<Item> itemTag = TagKey.create(Registries.ITEM, tagId);
        Optional<HolderSet.Named<Item>> itemEntries = BuiltInRegistries.ITEM.getTag(itemTag);
        if (itemEntries.isPresent()) {
            for (Holder<Item> holder : itemEntries.get()) {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(holder.value());
                if (!NetherendingOres.MOD_ID.equals(id.getNamespace())) {
                    return true;
                }
            }
        }

        TagKey<Block> blockTag = TagKey.create(Registries.BLOCK, tagId);
        Optional<HolderSet.Named<Block>> blockEntries = BuiltInRegistries.BLOCK.getTag(blockTag);
        if (blockEntries.isPresent()) {
            for (Holder<Block> holder : blockEntries.get()) {
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(holder.value());
                if (!NetherendingOres.MOD_ID.equals(id.getNamespace())) {
                    return true;
                }
            }
        }

        return false;
    }
}
