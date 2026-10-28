package org.icannt.netherendingores.common.event;

import org.icannt.netherendingores.NetherendingOres;
import org.icannt.netherendingores.common.NetherOreMechanics;
import org.icannt.netherendingores.common.entity.ArmedNetherOreEntity;
import org.icannt.netherendingores.lib.NeoLog;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = NetherendingOres.MOD_ID)
public final class NetherOreEvents {

    private static MinecraftServer trackedServer;
    private static Difficulty lastDifficulty;

    private NetherOreEvents() {
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (!NetherOreMechanics.isNetherOre(event.getState())) {
            return;
        }

        ServerLevel level = event.getLevel();
        BlockPos pos = event.getPos();
        boolean silkTouch = NetherOreMechanics.hasSilkTouch(level, event.getTool());
        ArmedNetherOreEntity armedOre = NetherOreMechanics.findArmedOre(level, pos);

        if (armedOre != null) {
            if (silkTouch) {
                // Silk Touch can safely defuse an ore that was already primed.
                armedOre.discard();
            } else {
                // Mining a primed ore still yields a drop, but it is a
                // convincing fake that cannot be used.
                NetherOreMechanics.replaceWithUnstableDrops(event);
            }
        }

        // Silk Touch completely avoids the usual Nether ore hazards.
        if (silkTouch) {
            return;
        }

        ServerPlayer player = event.getBreaker() instanceof ServerPlayer serverPlayer
                && isRealSurvivalPlayer(serverPlayer) ? serverPlayer : null;
        if (player != null) {
            int angered = NetherOreMechanics.angerPiglins(level, pos, player);
            if (angered > 0) {
                NeoLog.info("Angered " + angered + " Zombified Piglins after mining "
                        + event.getState().getBlock().getName().getString() + " at " + pos.toShortString());
            }

            // Machines and automation often break blocks through fake players.
            // Only a real survival player should start the mining hazards.
            NetherOreMechanics.armNearbyOres(level, pos);
        }
    }

    /**
     * Machines, drills, and some automation mods break blocks through a fake
     * player. Those should not make the real player the target of Piglin anger.
     * Creative and spectator players are also excluded so building in the
     * Nether does not trigger combat.
     */
    private static boolean isRealSurvivalPlayer(ServerPlayer player) {
        if (player instanceof FakePlayer || player.isCreative() || player.isSpectator()) {
            return false;
        }
        MinecraftServer server = player.getServer();
        return server != null && server.getPlayerList().getPlayer(player.getUUID()) == player;
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            NetherOreMechanics.clearAngerAt(player);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof ZombifiedPiglin piglin)) {
            return;
        }
        if (!(event.getLevel() instanceof ServerLevel)) {
            return;
        }

        // Our anger is a short-lived local hazard. Do not carry saved anger
        // across chunk unloads or world sessions; otherwise a player can be
        // attacked by Piglins long after the ore was mined.
        if (piglin.isAngry() || piglin.getPersistentAngerTarget() != null) {
            NetherOreMechanics.clearPiglinAnger(piglin);
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        NetherOreMechanics.armFromExplosion(level, event.getAffectedBlocks());
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        // DifficultyChangeEvent is only fired on the client side in 1.21.1, so
        // watch the server's authoritative difficulty here instead.
        MinecraftServer server = event.getServer();
        Difficulty current = server.getWorldData().getDifficulty();

        if (trackedServer != server) {
            trackedServer = server;
            lastDifficulty = current;
            if (current == Difficulty.PEACEFUL) {
                NetherOreMechanics.clearHostility(server);
            }
            return;
        }

        if (lastDifficulty != current) {
            lastDifficulty = current;
            if (current == Difficulty.PEACEFUL) {
                NetherOreMechanics.clearHostility(server);
            }
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        trackedServer = null;
        lastDifficulty = null;
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (NetherOreMechanics.isUnstable(event.getItemStack())) {
            event.setUseBlock(TriState.FALSE);
            event.setUseItem(TriState.FALSE);
            event.setCanceled(true);
        }
    }
}
