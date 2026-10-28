package org.icannt.netherendingores.datagen;

import java.util.List;
import java.util.Set;

import org.icannt.netherendingores.NetherendingOres;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = NetherendingOres.MOD_ID)
public class DataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if (event.includeServer()) {
            NeoBlockTags blockTags = new NeoBlockTags(output, lookupProvider, helper);
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new NeoItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
            generator.addProvider(true, new LootTableProvider(
                    output,
                    Set.of(),
                    List.of(new LootTableProvider.SubProviderEntry(NeoLootTables::new, LootContextParamSets.BLOCK)),
                    lookupProvider
            ));
            generator.addProvider(true, new NeoWorldGen(output, lookupProvider));
        }

        if (event.includeClient()) {
            generator.addProvider(true, new NeoBlockStates(output, helper));
            generator.addProvider(true, new NeoItemModels(output, helper));
            generator.addProvider(true, new NeoLanguages(output, "en_us"));
        }
    }

}
