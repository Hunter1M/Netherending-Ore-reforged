package org.icannt.netherendingores.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.icannt.netherendingores.Registration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Passes when the configured item tag exists and contains at least one item.
 * Used to fall back to the ore block when no mod supplies the matching drop tag.
 */
public record TagNotEmptyCondition(TagKey<Item> tag) implements LootItemCondition, LootItemCondition.Builder {

    public static final MapCodec<TagNotEmptyCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(TagNotEmptyCondition::tag)
    ).apply(instance, TagNotEmptyCondition::new));

    @Override
    public LootItemConditionType getType() {
        return Registration.TAG_NOT_EMPTY.get();
    }

    @Override
    public boolean test(LootContext context) {
        return BuiltInRegistries.ITEM.getTagOrEmpty(this.tag).iterator().hasNext();
    }

    @Override
    public LootItemCondition build() {
        return this;
    }
}
