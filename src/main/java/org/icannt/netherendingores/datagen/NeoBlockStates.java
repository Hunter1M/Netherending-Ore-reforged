package org.icannt.netherendingores.datagen;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.Registration;
import org.icannt.netherendingores.common.block.NeoOreBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NeoBlockStates extends BlockStateProvider {

    public NeoBlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, NetherendingOres.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            if ("ancient_debris".equals(ore.getOreType())) {
                // Ancient Debris has separate side and top textures.
                ResourceLocation side = modLoc(ore.getModelLocation("side"));
                ResourceLocation top = modLoc(ore.getModelLocation("top"));
                simpleBlock(ore.getBlock(), models().cubeColumn(ore.getRegistryBlockPath(), side, top));
            } else {
                simpleBlock(ore.getBlock());
            }
        }

        String path = "block/exploding_nether_ores_creative_tab_block_";
        ResourceLocation north = modLoc(path + "north");
        ResourceLocation south = modLoc(path + "south");
        ResourceLocation east = modLoc(path + "east");
        ResourceLocation west = modLoc(path + "west");
        simpleBlock(Registration.EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK.get(), models().cube(
                Registration.EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK.getId().getPath(),
                north, east, north, south, east, west
        ));
    }

}
