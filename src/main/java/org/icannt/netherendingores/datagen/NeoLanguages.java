package org.icannt.netherendingores.datagen;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.Registration;
import org.icannt.netherendingores.common.block.NeoOreBlocks;
import org.icannt.netherendingores.lib.NeoString;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class NeoLanguages extends LanguageProvider {

    public NeoLanguages(PackOutput output, String locale) {
        super(output, NetherendingOres.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + NetherendingOres.MOD_ID, NetherendingOres.MOD_NAME);

        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            String name = ore.getName();
            if ("ancient_debris".equals(ore.getOreType())) {
                // Ancient Debris doesn't have "Ore" in the name; it is retained in code for convenience.
                name = name.replace("_ore", "");
            }
            add(ore.getBlock(), NeoString.toTitleSpaced(name));
        }

        add(Registration.EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK.get(),
                NeoString.toTitleSpaced(Registration.EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK.getId().getPath()));

        add("item." + NetherendingOres.MOD_ID + ".unstable_ore.desc", "Unstable - cannot be placed or used");
    }

}
