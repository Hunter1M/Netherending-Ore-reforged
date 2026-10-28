package org.icannt.netherendingores.common.block;

import java.util.ArrayList;
import java.util.List;

import org.icannt.netherendingores.lib.NeoString;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public enum NeoOreBlocks implements IOreType {

    OVERWORLD_ANCIENT_DEBRIS_ORE ("ancient_debris", "overworld", 0, 0, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    DEEPSLATE_ANCIENT_DEBRIS_ORE ("ancient_debris", "deepslate", 0, 0, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_ANCIENT_DEBRIS_ORE ("ancient_debris", "end", 0, 2, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    NETHER_COAL_ORE ("coal", "nether", 0, 0, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_COAL_ORE ("coal", "end", 0, 0, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    NETHER_COPPER_ORE ("copper", "nether", 0, 1, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_COPPER_ORE ("copper", "end", 0, 1, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    NETHER_DIAMOND_ORE ("diamond", "nether", 0, 2, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_DIAMOND_ORE ("diamond", "end", 0, 2, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    NETHER_EMERALD_ORE ("emerald", "nether", 0, 2, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_EMERALD_ORE ("emerald", "end", 0, 2, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_GOLD_ORE ("gold", "end", 0, 2, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    NETHER_IRON_ORE ("iron", "nether", 0, 1, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_IRON_ORE ("iron", "end", 0, 1, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    NETHER_LAPIS_ORE ("lapis", "nether", 0, 1, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_LAPIS_ORE ("lapis", "end", 0, 1, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    OVERWORLD_QUARTZ_ORE ("quartz", "overworld", 0, 0, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    DEEPSLATE_QUARTZ_ORE ("quartz", "deepslate", 0, 0, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_QUARTZ_ORE ("quartz", "end", 0, 0, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    NETHER_REDSTONE_ORE ("redstone", "nether", 0, 1, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    END_REDSTONE_ORE ("redstone", "end", 0, 1, 3F, 15F, Rarity.COMMON, true, new String[0], "", ""),
    NETHER_AMETHYST_ORE ("amethyst", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    END_AMETHYST_ORE ("amethyst", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    NETHER_APATITE_ORE ("apatite", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    END_APATITE_ORE ("apatite", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    NETHER_CINNABAR_ORE ("cinnabar", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", "dusts"),
    END_CINNABAR_ORE ("cinnabar", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", "dusts"),
    OVERWORLD_COBALT_ORE ("cobalt", "overworld", 0, 4, 10F, 50F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    DEEPSLATE_COBALT_ORE ("cobalt", "deepslate", 0, 4, 10F, 50F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_COBALT_ORE ("cobalt", "end", 0, 4, 10F, 50F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    NETHER_FLUORITE_ORE ("fluorite", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", "dusts"),
    END_FLUORITE_ORE ("fluorite", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", "dusts"),
    NETHER_GREEN_SAPPHIRE_ORE ("green_sapphire", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    END_GREEN_SAPPHIRE_ORE ("green_sapphire", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    NETHER_LEAD_ORE ("lead", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_LEAD_ORE ("lead", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    NETHER_NICKEL_ORE ("nickel", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_NICKEL_ORE ("nickel", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    NETHER_NITER_ORE ("niter", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", "dusts"),
    END_NITER_ORE ("niter", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", "dusts"),
    NETHER_OSMIUM_ORE ("osmium", "nether", 0, 0, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_OSMIUM_ORE ("osmium", "end", 0, 0, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    NETHER_RUBY_ORE ("ruby", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    END_RUBY_ORE ("ruby", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    NETHER_SAPPHIRE_ORE ("sapphire", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    END_SAPPHIRE_ORE ("sapphire", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", ""),
    NETHER_SILVER_ORE ("silver", "nether", 4, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_SILVER_ORE ("silver", "end", 4, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    NETHER_SULFUR_ORE ("sulfur", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", "dusts"),
    END_SULFUR_ORE ("sulfur", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "gems", "dusts"),
    NETHER_TESLATITE_ORE ("teslatite", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "dusts", ""),
    END_TESLATITE_ORE ("teslatite", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "dusts", ""),
    NETHER_TIN_ORE ("tin", "nether", 0, 1, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_TIN_ORE ("tin", "end", 0, 1, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    NETHER_TUNGSTEN_ORE ("tungsten", "nether", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_TUNGSTEN_ORE ("tungsten", "end", 0, 2, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    NETHER_URANIUM_ORE ("uranium", "nether", 0, 2, 4F, 30F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_URANIUM_ORE ("uranium", "end", 0, 2, 4F, 30F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    DEEPSLATE_YELLORITE_ORE ("yellorite", "deepslate", 0, 0, 2F, 10F, Rarity.COMMON, false, new String[] {"yellorium"}, "raw_materials", "ingots"),
    NETHER_YELLORITE_ORE ("yellorite", "nether", 0, 0, 2F, 10F, Rarity.COMMON, false, new String[] {"yellorium"}, "raw_materials", "ingots"),
    END_YELLORITE_ORE ("yellorite", "end", 0, 0, 2F, 10F, Rarity.COMMON, false, new String[] {"yellorium"}, "raw_materials", "ingots"),
    NETHER_ZINC_ORE ("zinc", "nether", 0, 1, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots"),
    END_ZINC_ORE ("zinc", "end", 0, 1, 3F, 15F, Rarity.COMMON, false, new String[0], "raw_materials", "ingots");

    private static final String COMMON_NAMESPACE = "c";

    private final String name;
    private final String oreBaseType;
    private final int lightLevel;
    private final int harvestLevel;
    private final float hardness;
    private final float resistance;
    private final Rarity rarity;
    private final boolean isVanilla;
    private final String[] oreAltType;
    private final String itemTagMaterial;
    private final String itemTagAltMaterial;

    private DeferredBlock<Block> block;
    private DeferredItem<BlockItem> item;
    private TagKey<Block> blockTag;
    private TagKey<Item> itemTag;

    private enum VanillaOres {

        ORES_COAL (Tags.Blocks.ORES_COAL, Tags.Items.ORES_COAL, Items.COAL),
        ORES_COPPER (Tags.Blocks.ORES_COPPER, Tags.Items.ORES_COPPER, Items.RAW_COPPER),
        ORES_DIAMOND (Tags.Blocks.ORES_DIAMOND, Tags.Items.ORES_DIAMOND, Items.DIAMOND),
        ORES_EMERALD (Tags.Blocks.ORES_EMERALD, Tags.Items.ORES_EMERALD, Items.EMERALD),
        ORES_GOLD (Tags.Blocks.ORES_GOLD, Tags.Items.ORES_GOLD, Items.RAW_GOLD),
        ORES_IRON (Tags.Blocks.ORES_IRON, Tags.Items.ORES_IRON, Items.RAW_IRON),
        ORES_LAPIS (Tags.Blocks.ORES_LAPIS, Tags.Items.ORES_LAPIS, Items.LAPIS_LAZULI),
        ORES_ANCIENT_DEBRIS (Tags.Blocks.ORES_NETHERITE_SCRAP, Tags.Items.ORES_NETHERITE_SCRAP, Items.NETHERITE_SCRAP),
        ORES_QUARTZ (Tags.Blocks.ORES_QUARTZ, Tags.Items.ORES_QUARTZ, Items.QUARTZ),
        ORES_REDSTONE (Tags.Blocks.ORES_REDSTONE, Tags.Items.ORES_REDSTONE, Items.REDSTONE);

        private final TagKey<Block> blockTagKey;
        private final TagKey<Item> itemTagKey;
        private final Item lootItem;

        VanillaOres(final TagKey<Block> blockTagKey, final TagKey<Item> itemTagKey, final Item lootItem) {
            this.blockTagKey = blockTagKey;
            this.itemTagKey = itemTagKey;
            this.lootItem = lootItem;
        }

        private static TagKey<Block> getBlockTagKey(String oreType) {
            return VanillaOres.valueOf(oreType).blockTagKey;
        }

        private static TagKey<Item> getItemTagKey(String oreType) {
            return VanillaOres.valueOf(oreType).itemTagKey;
        }

        private static Item getLootItem(String oreType) {
            return VanillaOres.valueOf(oreType).lootItem;
        }
    }

    NeoOreBlocks(final String name, final String oreBaseType,
                 int lightLevel, int harvestLevel, float hardness, float resistance, Rarity rarity,
                 boolean isVanilla, String[] oreAltType, String itemTagMaterial, final String itemTagAltMaterial) {

        this.name = name;
        this.oreBaseType = oreBaseType;

        this.lightLevel = lightLevel;
        this.harvestLevel = harvestLevel;
        this.hardness = hardness;
        this.resistance = resistance;
        this.rarity = rarity;

        this.isVanilla = isVanilla;
        this.oreAltType = oreAltType;
        this.itemTagMaterial = itemTagMaterial;
        this.itemTagAltMaterial = itemTagAltMaterial;
    }

    @Override
    public String getName() {
        return NeoString.toLower(this.toString());
    }

    public String getOreType() {
        return this.name;
    }

    private String getOreBaseType() {
        return this.oreBaseType;
    }

    private float getHardness() {
        return this.hardness;
    }

    private float getResistance() {
        return this.resistance;
    }

    private int getLightLevel() {
        return this.lightLevel;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    private Rarity getRarity() {
        return this.rarity;
    }

    public boolean isVanilla() {
        return this.isVanilla;
    }

    public boolean isNether() {
        return "nether".equals(this.oreBaseType);
    }

    private String[] getOreAltType() {
        return this.oreAltType;
    }

    private String getItemTagMaterial() {
        return this.itemTagMaterial;
    }

    private String getItemTagAltMaterial() {
        return this.itemTagAltMaterial;
    }

    public TagKey<Block> getToolNeeded() {
        return switch (getHarvestLevel()) {
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            case 4 -> Tags.Blocks.NEEDS_NETHERITE_TOOL;
            default -> BlockTags.NEEDS_STONE_TOOL;
        };
    }

    private DeferredBlock<Block> getRegistryBlock() {
        return this.block;
    }

    private DeferredItem<BlockItem> getRegistryItem() {
        return this.item;
    }

    public Block getBlock() {
        return getRegistryBlock().get();
    }

    public Item getItem() {
        return getRegistryItem().get();
    }

    public String getRegistryBlockPath() {
        return getName();
    }

    public String getModelLocation() {
        return "block/" + getName();
    }

    public String getModelLocation(String data) {
        return "block/" + getName() + "_" + data;
    }

    public void setBlock(DeferredBlock<Block> blockRegistryObject, TagKey<Block> blockTagKey) {
        this.block = blockRegistryObject;
        this.blockTag = blockTagKey;
    }

    public void setItem(DeferredItem<BlockItem> itemRegistryObject, TagKey<Item> itemTagKey) {
        this.item = itemRegistryObject;
        this.itemTag = itemTagKey;
    }

    public TagKey<Block> getBlockTagKey() {
        return this.blockTag;
    }

    public TagKey<Item> getItemTagKey() {
        return this.itemTag;
    }

    private MapColor getMaterialColor() {
        return switch (getOreBaseType()) {
            case "deepslate" -> MapColor.DEEPSLATE;
            case "nether" -> MapColor.NETHER;
            case "end" -> MapColor.SAND;
            default -> MapColor.STONE;
        };
    }

    private SoundType getSound() {
        return switch (getOreBaseType()) {
            case "deepslate" -> SoundType.DEEPSLATE;
            case "nether" -> "gems".equals(getItemTagMaterial()) ? SoundType.NETHER_ORE : SoundType.NETHER_GOLD_ORE;
            default -> SoundType.STONE;
        };
    }

    public BlockBehaviour.Properties getBlockProperties() {
        // Nether ores use the classic low blast resistance so their armed
        // explosions can destroy and chain into adjacent Nether ores.
        float blastResistance = isNether() ? 1.0F : getResistance();
        return BlockBehaviour.Properties.of()
                .mapColor(getMaterialColor())
                .strength(getHardness(), blastResistance)
                .requiresCorrectToolForDrops()
                .lightLevel(state -> getLightLevel())
                .sound(getSound());
    }

    public Item.Properties getItemProperties() {
        return new Item.Properties()
                .stacksTo(64)
                .rarity(getRarity());
    }

    private String getVanillaOreTagName() {
        return "ORES_" + NeoString.toUpper(getOreType());
    }

    public TagKey<Block> getOreBlockTag() {
        if (isVanilla()) {
            return VanillaOres.getBlockTagKey(getVanillaOreTagName());
        }
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(COMMON_NAMESPACE, "ores/" + getOreType()));
    }

    public TagKey<Item> getOreItemTag() {
        if (isVanilla()) {
            return VanillaOres.getItemTagKey(getVanillaOreTagName());
        }
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(COMMON_NAMESPACE, "ores/" + getOreType()));
    }

    public Item getVanillaLootItem() {
        return VanillaOres.getLootItem(getVanillaOreTagName());
    }

    public String getSmeltingGroup() {
        return getOreType() + "_" + "ingot";
    }

    public TagKey<Item> getOreLootItemTag() {
        return getOreLootItemTags().get(0);
    }

    /**
     * Material item tags that can supply this ore's drops. The first tag is the
     * preferred drop; later tags are compatibility fallbacks such as dusts for
     * Thermal-style materials, ingots for metals, peridot for green sapphire,
     * and yellorium for yellorite.
     */
    public List<TagKey<Item>> getOreLootItemTags() {
        if ("green_sapphire".equals(getOreType())) {
            return List.of(
                    commonItemTag("gems", "green_sapphire"),
                    commonItemTag("gems", "peridot")
            );
        }

        if ("yellorite".equals(getOreType())) {
            // Extreme Reactors calls the material yellorium, not yellorite.
            return List.of(
                    commonItemTag("raw_materials", "yellorium"),
                    commonItemTag("ingots", "yellorium")
            );
        }

        List<TagKey<Item>> tags = new ArrayList<>();
        tags.add(commonItemTag(getItemTagMaterial(), getOreType()));
        if (!getItemTagAltMaterial().isEmpty()) {
            tags.add(commonItemTag(getItemTagAltMaterial(), getOreType()));
        }
        return List.copyOf(tags);
    }

    private TagKey<Item> commonItemTag(String material, String type) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(COMMON_NAMESPACE, material + "/" + type));
    }

    public static boolean isNeoOre(String path) {
        try {
            NeoOreBlocks.valueOf(NeoString.toUpper(path));
        } catch (Exception error) {
            return false;
        }
        return true;
    }
}
