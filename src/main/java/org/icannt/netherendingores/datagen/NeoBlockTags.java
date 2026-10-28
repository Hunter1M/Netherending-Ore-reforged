package org.icannt.netherendingores.datagen;

import java.util.concurrent.CompletableFuture;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.Registration;
import org.icannt.netherendingores.common.block.NeoOreBlocks;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NeoBlockTags extends BlockTagsProvider {

    public NeoBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper helper) {
        super(output, lookupProvider, NetherendingOres.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            tag(Tags.Blocks.ORES).add(ore.getBlock());
            tag(ore.getOreBlockTag()).add(ore.getBlock());
            tag(ore.getBlockTagKey()).add(ore.getBlock());
            if (ore.isNether()) {
                tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(ore.getBlock());
            }
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ore.getBlock());
            if (ore.getHarvestLevel() > 0) {
                tag(ore.getToolNeeded()).add(ore.getBlock());
            }
        }

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK.get());
    }

}
