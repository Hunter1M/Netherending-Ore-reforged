package org.icannt.netherendingores.common;

import java.util.ArrayList;
import java.util.List;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.Registration;
import org.icannt.netherendingores.common.block.NeoOreBlocks;
import org.icannt.netherendingores.common.entity.ArmedNetherOreEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

/**
 * Shared logic for the classic Nether ore hazards: angry Zombified Piglins,
 * armed ores, chain reactions and unstable fake drops.
 */
public final class NetherOreMechanics {

    private static final DifficultySettings PEACEFUL_SETTINGS = new DifficultySettings(0, 0.0F, 0.0F, false);
    private static final DifficultySettings EASY_SETTINGS = new DifficultySettings(12, 9.0F, 1.0F, false);
    private static final DifficultySettings NORMAL_SETTINGS = new DifficultySettings(24, 7.5F, 2.0F, true);
    private static final DifficultySettings HARD_SETTINGS = new DifficultySettings(32, 15.0F, 3.0F, true);

    private NetherOreMechanics() {
    }

    public record DifficultySettings(int angerRange, float explosionChance, float explosionPower, boolean chainReactions) {
    }

    public static DifficultySettings settingsFor(Difficulty difficulty) {
        return switch (difficulty) {
            case PEACEFUL -> PEACEFUL_SETTINGS;
            case EASY -> EASY_SETTINGS;
            case NORMAL -> NORMAL_SETTINGS;
            case HARD -> HARD_SETTINGS;
        };
    }

    public static boolean isNetherOre(BlockState state) {
        return isNetherOre(state.getBlock());
    }

    public static boolean isNetherOre(Block block) {
        for (NeoOreBlocks ore : NeoOreBlocks.values()) {
            if (ore.isNether() && ore.getBlock() == block) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasSilkTouch(ServerLevel level, ItemStack tool) {
        if (tool.isEmpty()) {
            return false;
        }

        Holder<Enchantment> silkTouch = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.SILK_TOUCH);
        return tool.getEnchantmentLevel(silkTouch) > 0;
    }

    /**
     * Angers nearby Zombified Piglins even when they could not see the player.
     *
     * @return the number of Piglins that were angered
     */
    public static int angerPiglins(ServerLevel level, BlockPos pos, Player player) {
        DifficultySettings settings = settingsFor(level.getDifficulty());
        if (player == null || settings.angerRange() <= 0) {
            return 0;
        }

        AABB area = new AABB(pos).inflate(settings.angerRange());
        List<ZombifiedPiglin> piglins = level.getEntitiesOfClass(ZombifiedPiglin.class, area, ZombifiedPiglin::isAlive);
        for (ZombifiedPiglin piglin : piglins) {
            piglin.setPersistentAngerTarget(player.getUUID());
            piglin.startPersistentAngerTimer();
            piglin.setTarget(player);
            piglin.setLastHurtByMob(player);
            piglin.setLastHurtByPlayer(player);
        }
        return piglins.size();
    }

    /**
     * Clears anger that is still pointing at a player who has just joined. This
     * prevents anger saved in unloaded chunks from surprising the player on a
     * later login.
     */
    public static void clearAngerAt(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        for (ServerLevel level : server.getAllLevels()) {
            List<Entity> entities = new ArrayList<>();
            level.getAllEntities().forEach(entities::add);
            for (Entity entity : entities) {
                if (entity instanceof ZombifiedPiglin piglin
                        && (piglin.isAngry() || piglin.getPersistentAngerTarget() != null)
                        && (player.getUUID().equals(piglin.getPersistentAngerTarget())
                                || piglin.getTarget() == player
                                || piglin.getPersistentAngerTarget() == null)) {
                    clearPiglinAnger(piglin);
                }
            }
        }
    }

    public static void clearPiglinAnger(ZombifiedPiglin piglin) {
        piglin.stopBeingAngry();
        piglin.setTarget(null);
        piglin.setLastHurtByMob(null);
        piglin.setLastHurtByPlayer(null);
    }

    /**
     * Rolls the explosion chance for every Nether ore directly adjacent to the
     * block that was just mined.
     */
    public static void armNearbyOres(ServerLevel level, BlockPos origin) {
        DifficultySettings settings = settingsFor(level.getDifficulty());
        if (settings.explosionChance() <= 0.0F) {
            return;
        }

        boolean armedAny = false;
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    if (xOffset == 0 && yOffset == 0 && zOffset == 0) {
                        continue;
                    }

                    BlockPos targetPos = origin.offset(xOffset, yOffset, zOffset);
                    BlockState targetState = level.getBlockState(targetPos);
                    if (!isNetherOre(targetState)) {
                        continue;
                    }
                    if (level.random.nextFloat() * 100.0F >= settings.explosionChance()) {
                        continue;
                    }

                    if (armOre(level, targetPos, targetState)) {
                        armedAny = true;
                    }
                }
            }
        }

        if (armedAny) {
            level.playSound(null, origin, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    /**
     * Arms ores caught in an explosion. This is what turns an armed ore into a
     * chain reaction on Normal and Hard difficulty.
     */
    public static void armFromExplosion(ServerLevel level, List<BlockPos> affectedBlocks) {
        DifficultySettings settings = settingsFor(level.getDifficulty());
        if (!settings.chainReactions() || settings.explosionChance() <= 0.0F) {
            return;
        }

        for (BlockPos pos : affectedBlocks) {
            BlockState state = level.getBlockState(pos);
            if (!isNetherOre(state)) {
                continue;
            }
            if (level.random.nextFloat() * 100.0F >= settings.explosionChance()) {
                continue;
            }

            armOre(level, pos, state);
        }
    }

    private static boolean armOre(ServerLevel level, BlockPos pos, BlockState state) {
        if (level.getDifficulty() == Difficulty.PEACEFUL || !isNetherOre(state)) {
            return false;
        }
        if (findArmedOre(level, pos) != null) {
            return false;
        }

        EntityType<ArmedNetherOreEntity> entityType = Registration.ARMED_NETHER_ORE.get();
        ArmedNetherOreEntity entity = entityType.create(level);
        if (entity == null) {
            return false;
        }

        entity.setTargetPos(pos);
        entity.moveTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0F, 0.0F);
        return level.addFreshEntity(entity);
    }

    public static ArmedNetherOreEntity findArmedOre(ServerLevel level, BlockPos pos) {
        AABB area = new AABB(pos).inflate(1.0D);
        for (ArmedNetherOreEntity entity : level.getEntitiesOfClass(ArmedNetherOreEntity.class, area)) {
            if (!entity.isRemoved() && pos.equals(entity.getTargetPos())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Replaces all normal drops from an armed ore with non-stackable fake ore
     * items that look exactly like the real thing.
     */
    public static void replaceWithUnstableDrops(BlockDropsEvent event) {
        List<ItemEntity> drops = event.getDrops();
        List<ItemEntity> replacements = new ArrayList<>(drops.size());

        for (ItemEntity drop : drops) {
            ItemStack original = drop.getItem();
            int count = Math.max(1, original.getCount());
            ItemStack unstable = createUnstableStack(event.getState());
            if (unstable.isEmpty()) {
                replacements.add(drop);
                continue;
            }

            for (int i = 0; i < count; i++) {
                ItemEntity replacement = new ItemEntity(event.getLevel(),
                        drop.getX(), drop.getY(), drop.getZ(), unstable.copyWithCount(1));
                replacement.setDeltaMovement(drop.getDeltaMovement());
                replacements.add(replacement);
            }
        }

        drops.clear();
        drops.addAll(replacements);
    }

    public static ItemStack createUnstableStack(BlockState state) {
        Item item = state.getBlock().asItem();
        if (item == Items.AIR) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(item);
        stack.set(DataComponents.MAX_STACK_SIZE, 1);
        stack.set(Registration.UNSTABLE_ORE.get(), Unit.INSTANCE);
        stack.set(DataComponents.LORE, new ItemLore(List.of(
                Component.translatable("item." + NetherendingOres.MOD_ID + ".unstable_ore.desc")
        )));
        return stack;
    }

    public static boolean isUnstable(ItemStack stack) {
        return !stack.isEmpty() && stack.has(Registration.UNSTABLE_ORE.get());
    }

    /**
     * Clears all accumulated piglin anger and defuses armed ores. Called when
     * the world is switched to Peaceful.
     */
    public static void clearHostility(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            List<Entity> entities = new ArrayList<>();
            level.getAllEntities().forEach(entities::add);

            for (Entity entity : entities) {
                if (entity instanceof ArmedNetherOreEntity armed) {
                    armed.discard();
                } else if (entity instanceof ZombifiedPiglin piglin) {
                    clearPiglinAnger(piglin);
                }
            }
        }
    }
}
