package org.icannt.netherendingores.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.Registration;
import org.icannt.netherendingores.worldgen.ConditionalOreConfiguration;
import org.icannt.netherendingores.worldgen.NetherOreWorldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NeoWorldGen extends DatapackBuiltinEntriesProvider {

    private static final ResourceKey<BiomeModifier> NETHER_ORES = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            ResourceLocation.fromNamespaceAndPath(NetherendingOres.MOD_ID, "nether_ores")
    );

    public NeoWorldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, createBuilder(), Set.of(NetherendingOres.MOD_ID));
    }

    private static RegistrySetBuilder createBuilder() {
        return new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, NeoWorldGen::bootstrapConfiguredFeatures)
                .add(Registries.PLACED_FEATURE, NeoWorldGen::bootstrapPlacedFeatures)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NeoWorldGen::bootstrapBiomeModifiers);
    }

    private static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        TagMatchTest netherrack = new TagMatchTest(Tags.Blocks.NETHERRACKS);

        for (NetherOreWorldgen.Settings settings : NetherOreWorldgen.settings()) {
            OreConfiguration configuration = new OreConfiguration(
                    netherrack,
                    settings.ore().getBlock().defaultBlockState(),
                    settings.veinSize(),
                    0.0F
            );

            NetherOreWorldgen.WorldgenCondition condition = NetherOreWorldgen.condition(settings.ore());
            if (condition.always()) {
                context.register(
                        NetherOreWorldgen.configuredFeature(settings.ore()),
                        new ConfiguredFeature<>(Feature.ORE, configuration)
                );
            } else {
                context.register(
                        NetherOreWorldgen.configuredFeature(settings.ore()),
                        new ConfiguredFeature<>(Registration.CONDITIONAL_ORE.get(),
                                new ConditionalOreConfiguration(configuration, condition.mods(), condition.tags()))
                );
            }
        }
    }

    private static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        for (NetherOreWorldgen.Settings settings : NetherOreWorldgen.settings()) {
            List<PlacementModifier> placement = List.of(
                    CountPlacement.of(settings.countPerChunk()),
                    InSquarePlacement.spread(),
                    settings.distribution().createPlacement(settings.minY(), settings.maxY()),
                    BiomeFilter.biome()
            );

            context.register(
                    NetherOreWorldgen.placedFeature(settings.ore()),
                    new PlacedFeature(
                            configuredFeatures.getOrThrow(NetherOreWorldgen.configuredFeature(settings.ore())),
                            placement
                    )
            );
        }
    }

    private static void bootstrapBiomeModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        List<Holder<PlacedFeature>> featureHolders = NetherOreWorldgen.settings().stream()
                .<Holder<PlacedFeature>>map(settings -> placedFeatures.getOrThrow(NetherOreWorldgen.placedFeature(settings.ore())))
                .toList();

        context.register(
                NETHER_ORES,
                new AddFeaturesBiomeModifier(
                        biomes.getOrThrow(Tags.Biomes.IS_NETHER),
                        HolderSet.direct(featureHolders),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );
    }
}
