package org.icannt.netherendingores.worldgen;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

/**
 * Wraps a normal ore configuration with the mod IDs and tag IDs that make the
 * ore available. The wrapped ore only generates when at least one of those
 * conditions is present.
 */
public record ConditionalOreConfiguration(OreConfiguration ore, List<String> mods, List<ResourceLocation> tags)
        implements FeatureConfiguration {

    public static final Codec<ConditionalOreConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            OreConfiguration.CODEC.fieldOf("ore").forGetter(ConditionalOreConfiguration::ore),
            Codec.STRING.listOf().optionalFieldOf("mods", List.of()).forGetter(ConditionalOreConfiguration::mods),
            ResourceLocation.CODEC.listOf().optionalFieldOf("tags", List.of()).forGetter(ConditionalOreConfiguration::tags)
    ).apply(instance, ConditionalOreConfiguration::new));

    public ConditionalOreConfiguration {
        mods = List.copyOf(mods);
        tags = List.copyOf(tags);
    }
}
