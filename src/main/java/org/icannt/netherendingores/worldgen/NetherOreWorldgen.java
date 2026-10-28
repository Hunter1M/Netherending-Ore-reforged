package org.icannt.netherendingores.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.common.block.NeoOreBlocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

/**
 * Explicit default Nether worldgen values.
 *
 * <p>The values for ores that existed in the old CoFH World configs follow those files:
 * uniform height distribution, Y 10-118, netherrack target, all Nether biomes. Newer ores use
 * values chosen to match their material family and the legacy configs.</p>
 */
public final class NetherOreWorldgen {

    private static final int DEFAULT_MIN_Y = 10;
    private static final int DEFAULT_MAX_Y = 118;

    private NetherOreWorldgen() {
    }

    public enum HeightDistribution {
        UNIFORM {
            @Override
            public PlacementModifier createPlacement(int minY, int maxY) {
                return HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY));
            }
        },
        TRIANGLE {
            @Override
            public PlacementModifier createPlacement(int minY, int maxY) {
                return HeightRangePlacement.triangle(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY));
            }
        };

        public abstract PlacementModifier createPlacement(int minY, int maxY);
    }

    public record Settings(NeoOreBlocks ore, int veinSize, int countPerChunk, int minY, int maxY,
                           HeightDistribution distribution) {

        public Settings {
            if (ore == null || !ore.isNether()) {
                throw new IllegalArgumentException("Worldgen settings require a Nether ore");
            }
            if (veinSize < 1 || veinSize > 64) {
                throw new IllegalArgumentException("Vein size must be between 1 and 64");
            }
            if (countPerChunk < 1) {
                throw new IllegalArgumentException("Count per chunk must be positive");
            }
            if (minY > maxY) {
                throw new IllegalArgumentException("Minimum Y must not exceed maximum Y");
            }
        }

        private Settings(NeoOreBlocks ore, int veinSize, int countPerChunk) {
            this(ore, veinSize, countPerChunk, DEFAULT_MIN_Y, DEFAULT_MAX_Y, HeightDistribution.UNIFORM);
        }
    }

    /**
     * A worldgen gate for a Nether ore. The ore is generated when any listed mod
     * is installed, or when any listed item/block tag has an entry that does not
     * belong to this mod. Vanilla ores use {@link #VANILLA} and skip the gate.
     */
    public record WorldgenCondition(boolean always, List<String> mods, List<ResourceLocation> tags) {

        public static final WorldgenCondition VANILLA = new WorldgenCondition(true, List.of(), List.of());
        public static final WorldgenCondition NEVER = new WorldgenCondition(false, List.of(), List.of());

        public static WorldgenCondition modded(List<String> mods, NeoOreBlocks ore) {
            return new WorldgenCondition(false, List.copyOf(mods),
                    conditionTags(ore).stream().map(ResourceLocation::parse).toList());
        }
    }

    private static final List<Settings> SETTINGS = List.of(
            // Values from the historical CoFH World configs.
            new Settings(NeoOreBlocks.NETHER_COAL_ORE, 16, 8),
            new Settings(NeoOreBlocks.NETHER_COPPER_ORE, 8, 8),
            new Settings(NeoOreBlocks.NETHER_DIAMOND_ORE, 3, 4),
            new Settings(NeoOreBlocks.NETHER_EMERALD_ORE, 2, 3),
            new Settings(NeoOreBlocks.NETHER_IRON_ORE, 8, 8),
            new Settings(NeoOreBlocks.NETHER_LAPIS_ORE, 6, 6),
            new Settings(NeoOreBlocks.NETHER_REDSTONE_ORE, 8, 6),
            new Settings(NeoOreBlocks.NETHER_LEAD_ORE, 6, 6),
            new Settings(NeoOreBlocks.NETHER_NICKEL_ORE, 6, 4),
            new Settings(NeoOreBlocks.NETHER_OSMIUM_ORE, 7, 8),
            new Settings(NeoOreBlocks.NETHER_RUBY_ORE, 3, 6),
            new Settings(NeoOreBlocks.NETHER_SAPPHIRE_ORE, 3, 6),
            new Settings(NeoOreBlocks.NETHER_SILVER_ORE, 4, 6),
            new Settings(NeoOreBlocks.NETHER_TIN_ORE, 8, 8),
            new Settings(NeoOreBlocks.NETHER_URANIUM_ORE, 2, 3),
            new Settings(NeoOreBlocks.NETHER_YELLORITE_ORE, 2, 3),
            new Settings(NeoOreBlocks.NETHER_ZINC_ORE, 5, 3),

            // Newer ores, matched to the closest material family in the legacy configs.
            new Settings(NeoOreBlocks.NETHER_AMETHYST_ORE, 3, 4),
            new Settings(NeoOreBlocks.NETHER_APATITE_ORE, 6, 6),
            new Settings(NeoOreBlocks.NETHER_CINNABAR_ORE, 4, 4),
            new Settings(NeoOreBlocks.NETHER_FLUORITE_ORE, 4, 4),
            new Settings(NeoOreBlocks.NETHER_GREEN_SAPPHIRE_ORE, 3, 6),
            new Settings(NeoOreBlocks.NETHER_NITER_ORE, 4, 5),
            new Settings(NeoOreBlocks.NETHER_SULFUR_ORE, 4, 6),
            new Settings(NeoOreBlocks.NETHER_TESLATITE_ORE, 4, 8),
            new Settings(NeoOreBlocks.NETHER_TUNGSTEN_ORE, 4, 4)
    );

    private static final Map<NeoOreBlocks, Settings> BY_ORE = SETTINGS.stream()
            .collect(Collectors.toUnmodifiableMap(Settings::ore, Function.identity()));

    private static final List<String> TIN_MODS = List.of(
            "mekanism", "nuclearcraft", "electrodynamics", "ic2", "ic2classic", "ic2nostalgia",
            "thermal", "thermal_foundation", "thermalfoundation");
    private static final List<String> LEAD_MODS = List.of(
            "mekanism", "nuclearcraft", "electrodynamics", "ic2", "ic2classic", "ic2nostalgia",
            "thermal", "thermal_foundation", "thermalfoundation", "immersiveengineering");
    private static final List<String> SILVER_MODS = List.of(
            "nuclearcraft", "electrodynamics", "ic2classic", "ic2nostalgia",
            "thermal", "thermal_foundation", "thermalfoundation", "immersiveengineering");
    private static final List<String> NICKEL_MODS = List.of(
            "thermal", "thermal_foundation", "thermalfoundation", "immersiveengineering");
    private static final List<String> OSMIUM_MODS = List.of("mekanism");
    private static final List<String> URANIUM_MODS = List.of(
            "mekanism", "nuclearcraft", "electrodynamics", "ic2", "ic2classic", "ic2nostalgia",
            "bigreactors", "extremereactors", "biggerreactors", "immersiveengineering");
    private static final List<String> ZINC_MODS = List.of(
            "create", "nuclearcraft", "thermal", "thermal_foundation", "thermalfoundation");
    private static final List<String> YELLORITE_MODS = List.of(
            "bigreactors", "extremereactors", "biggerreactors");
    private static final List<String> APATITE_MODS = List.of(
            "thermal", "thermal_foundation", "thermalfoundation", "forestry");
    private static final List<String> CINNABAR_MODS = List.of(
            "thermal", "thermal_foundation", "thermalfoundation", "thaumcraft");
    private static final List<String> FLUORITE_MODS = List.of(
            "mekanism", "nuclearcraft", "electrodynamics",
            "thermal", "thermal_foundation", "thermalfoundation");
    private static final List<String> NITER_MODS = List.of(
            "electrodynamics", "thermal", "thermal_foundation", "thermalfoundation");
    private static final List<String> SULFUR_MODS = List.of(
            "mekanism", "nuclearcraft", "electrodynamics",
            "thermal", "thermal_foundation", "thermalfoundation");
    private static final List<String> PROJECT_RED_MODS = List.of("projectred_core", "projectred_exploration");
    private static final List<String> TUNGSTEN_MODS = List.of("nuclearcraft");

    private static final Map<NeoOreBlocks, WorldgenCondition> CONDITIONS = Map.ofEntries(
            Map.entry(NeoOreBlocks.NETHER_AMETHYST_ORE, WorldgenCondition.modded(List.of(), NeoOreBlocks.NETHER_AMETHYST_ORE)),
            Map.entry(NeoOreBlocks.NETHER_APATITE_ORE, WorldgenCondition.modded(APATITE_MODS, NeoOreBlocks.NETHER_APATITE_ORE)),
            Map.entry(NeoOreBlocks.NETHER_CINNABAR_ORE, WorldgenCondition.modded(CINNABAR_MODS, NeoOreBlocks.NETHER_CINNABAR_ORE)),
            Map.entry(NeoOreBlocks.NETHER_FLUORITE_ORE, WorldgenCondition.modded(FLUORITE_MODS, NeoOreBlocks.NETHER_FLUORITE_ORE)),
            Map.entry(NeoOreBlocks.NETHER_GREEN_SAPPHIRE_ORE, WorldgenCondition.modded(PROJECT_RED_MODS, NeoOreBlocks.NETHER_GREEN_SAPPHIRE_ORE)),
            Map.entry(NeoOreBlocks.NETHER_LEAD_ORE, WorldgenCondition.modded(LEAD_MODS, NeoOreBlocks.NETHER_LEAD_ORE)),
            Map.entry(NeoOreBlocks.NETHER_NICKEL_ORE, WorldgenCondition.modded(NICKEL_MODS, NeoOreBlocks.NETHER_NICKEL_ORE)),
            Map.entry(NeoOreBlocks.NETHER_NITER_ORE, WorldgenCondition.modded(NITER_MODS, NeoOreBlocks.NETHER_NITER_ORE)),
            Map.entry(NeoOreBlocks.NETHER_OSMIUM_ORE, WorldgenCondition.modded(OSMIUM_MODS, NeoOreBlocks.NETHER_OSMIUM_ORE)),
            Map.entry(NeoOreBlocks.NETHER_RUBY_ORE, WorldgenCondition.modded(PROJECT_RED_MODS, NeoOreBlocks.NETHER_RUBY_ORE)),
            Map.entry(NeoOreBlocks.NETHER_SAPPHIRE_ORE, WorldgenCondition.modded(PROJECT_RED_MODS, NeoOreBlocks.NETHER_SAPPHIRE_ORE)),
            Map.entry(NeoOreBlocks.NETHER_SILVER_ORE, WorldgenCondition.modded(SILVER_MODS, NeoOreBlocks.NETHER_SILVER_ORE)),
            Map.entry(NeoOreBlocks.NETHER_SULFUR_ORE, WorldgenCondition.modded(SULFUR_MODS, NeoOreBlocks.NETHER_SULFUR_ORE)),
            Map.entry(NeoOreBlocks.NETHER_TESLATITE_ORE, WorldgenCondition.modded(List.of(), NeoOreBlocks.NETHER_TESLATITE_ORE)),
            Map.entry(NeoOreBlocks.NETHER_TIN_ORE, WorldgenCondition.modded(TIN_MODS, NeoOreBlocks.NETHER_TIN_ORE)),
            Map.entry(NeoOreBlocks.NETHER_TUNGSTEN_ORE, WorldgenCondition.modded(TUNGSTEN_MODS, NeoOreBlocks.NETHER_TUNGSTEN_ORE)),
            Map.entry(NeoOreBlocks.NETHER_URANIUM_ORE, WorldgenCondition.modded(URANIUM_MODS, NeoOreBlocks.NETHER_URANIUM_ORE)),
            Map.entry(NeoOreBlocks.NETHER_YELLORITE_ORE, WorldgenCondition.modded(YELLORITE_MODS, NeoOreBlocks.NETHER_YELLORITE_ORE)),
            Map.entry(NeoOreBlocks.NETHER_ZINC_ORE, WorldgenCondition.modded(ZINC_MODS, NeoOreBlocks.NETHER_ZINC_ORE))
    );

    static {
        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            boolean hasSettings = BY_ORE.containsKey(ore);
            if (ore.isNether() != hasSettings) {
                throw new IllegalStateException("Missing or unexpected Nether worldgen settings for " + ore.getName());
            }
            if (ore.isNether() && !ore.isVanilla() && !CONDITIONS.containsKey(ore)) {
                throw new IllegalStateException("Missing worldgen condition for " + ore.getName());
            }
        }
    }

    public static List<Settings> settings() {
        return SETTINGS;
    }

    public static Settings get(NeoOreBlocks ore) {
        Settings settings = BY_ORE.get(ore);
        if (settings == null) {
            throw new IllegalArgumentException("No Nether worldgen settings for " + ore.getName());
        }
        return settings;
    }

    public static WorldgenCondition condition(NeoOreBlocks ore) {
        if (!ore.isNether()) {
            throw new IllegalArgumentException("No Nether worldgen condition for " + ore.getName());
        }
        if (ore.isVanilla()) {
            return WorldgenCondition.VANILLA;
        }
        WorldgenCondition condition = CONDITIONS.get(ore);
        return condition != null ? condition : WorldgenCondition.NEVER;
    }

    private static List<String> conditionTags(NeoOreBlocks ore) {
        if (ore == NeoOreBlocks.NETHER_AMETHYST_ORE) {
            // Vanilla has amethyst shards, but no amethyst ore. Only generate a
            // Nether amethyst ore when another mod actually adds an amethyst ore.
            return List.of("c:ores/amethyst");
        }

        List<String> tags = new ArrayList<>();
        for (TagKey<Item> tag : ore.getOreLootItemTags()) {
            tags.add(tag.location().toString());
        }
        tags.add("c:ores/" + ore.getOreType());

        if (ore == NeoOreBlocks.NETHER_GREEN_SAPPHIRE_ORE) {
            tags.add("c:ores/peridot");
        }
        return List.copyOf(tags);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> configuredFeature(NeoOreBlocks ore) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, id(ore));
    }

    public static ResourceKey<PlacedFeature> placedFeature(NeoOreBlocks ore) {
        return ResourceKey.create(Registries.PLACED_FEATURE, id(ore));
    }

    private static ResourceLocation id(NeoOreBlocks ore) {
        return ResourceLocation.fromNamespaceAndPath(NetherendingOres.MOD_ID, ore.getName());
    }
}
