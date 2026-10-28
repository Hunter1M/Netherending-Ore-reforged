package org.icannt.netherendingores.datagen;

import java.util.concurrent.CompletableFuture;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.common.block.NeoOreBlocks;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NeoItemTags extends ItemTagsProvider {

    public NeoItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                       CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper helper) {
        super(output, lookupProvider, blockTags, NetherendingOres.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            tag(Tags.Items.ORES).add(ore.getItem());
            tag(ore.getOreItemTag()).add(ore.getItem());
            tag(ore.getItemTagKey()).add(ore.getItem());
            if (ore.isNether()) {
                tag(Tags.Items.ORES_IN_GROUND_NETHERRACK).add(ore.getItem());
            }
        }
    }

}
