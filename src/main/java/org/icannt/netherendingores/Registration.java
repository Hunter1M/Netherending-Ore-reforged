package org.icannt.netherendingores;

import static org.icannt.netherendingores.NetherendingOres.MOD_ID;

import java.util.stream.Stream;

import org.icannt.netherendingores.common.block.NeoOreBlocks;
import org.icannt.netherendingores.common.entity.ArmedNetherOreEntity;
import org.icannt.netherendingores.common.loot.TagNotEmptyCondition;
import org.icannt.netherendingores.worldgen.ConditionalOreFeature;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registration {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MOD_ID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MOD_ID);

    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> TAG_NOT_EMPTY = LOOT_CONDITION_TYPES.register(
            "tag_not_empty",
            () -> new LootItemConditionType(TagNotEmptyCondition.CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> UNSTABLE_ORE = DATA_COMPONENTS.register(
            "unstable_ore",
            () -> DataComponentType.<Unit>builder()
                    .persistent(Unit.CODEC)
                    .networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
                    .build()
    );

    public static final DeferredHolder<EntityType<?>, EntityType<ArmedNetherOreEntity>> ARMED_NETHER_ORE = ENTITY_TYPES.register(
            "armed_nether_ore",
            () -> EntityType.Builder.of(ArmedNetherOreEntity::new, MobCategory.MISC)
                    .sized(0.0F, 0.0F)
                    .noSummon()
                    .fireImmune()
                    .setTrackingRange(10)
                    .setUpdateInterval(20)
                    .setShouldReceiveVelocityUpdates(false)
                    .build(MOD_ID + ":armed_nether_ore")
    );

    public static final DeferredHolder<Feature<?>, ConditionalOreFeature> CONDITIONAL_ORE = FEATURES.register(
            "conditional_ore",
            ConditionalOreFeature::new
    );

    public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(1.5F, 6.0F)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE);

    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties();

    public static final DeferredBlock<Block> EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK = BLOCKS.register(
            "exploding_nether_ores_creative_tab_block",
            () -> new Block(BLOCK_PROPERTIES)
    );

    public static final DeferredItem<BlockItem> EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK_ITEM = fromBlock(EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_NETHERENDING_ORES = CREATIVE_MODE_TABS.register(
            MOD_ID + "_item_group",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MOD_ID))
                    .icon(() -> new ItemStack(EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
                            output.accept(ore.getItem());
                        }
                        output.accept(EXPLODING_NETHER_ORES_CREATIVE_TAB_BLOCK_ITEM.get());
                    })
                    .build()
    );

    public static void init(IEventBus bus) {
        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            final DeferredBlock<Block> block = BLOCKS.register(ore.getName(), () -> new Block(ore.getBlockProperties()));
            final DeferredItem<BlockItem> item = ITEMS.register(ore.getName(), () -> new BlockItem(block.get(), ore.getItemProperties()));

            final TagKey<Block> blockTag = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, ore.getName()));
            final TagKey<Item> itemTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, ore.getName()));

            ore.setBlock(block, blockTag);
            ore.setItem(item, itemTag);
        }

        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        LOOT_CONDITION_TYPES.register(bus);
        DATA_COMPONENTS.register(bus);
        ENTITY_TYPES.register(bus);
        FEATURES.register(bus);
    }

    public static Stream<Block> getBlocks() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get);
    }

    public static <B extends Block> DeferredItem<BlockItem> fromBlock(DeferredBlock<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }
}
