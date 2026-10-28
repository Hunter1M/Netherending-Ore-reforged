package org.icannt.netherendingores.datagen;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.Registration;
import org.icannt.netherendingores.common.block.NeoOreBlocks;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NeoItemModels extends ItemModelProvider {

    public NeoItemModels(PackOutput output, ExistingFileHelper helper) {
        super(output, NetherendingOres.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            withExistingParent(ore.getRegistryBlockPath(), modLoc(ore.getModelLocation()));
        }

        String registryPath = Registration.EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK.getId().getPath();
        withExistingParent(registryPath, modLoc("block/" + registryPath));
    }

}
