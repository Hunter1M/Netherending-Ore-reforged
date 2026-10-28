package org.icannt.netherendingores.common.entity;

import org.icannt.netherendingores.common.NetherOreMechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

/**
 * Invisible timer entity used by Nether ores. The entity is spawned next to a
 * freshly mined ore, plays the TNT prime sound, and explodes after a short
 * fuse even if the ore block itself is removed in the meantime.
 */
public class ArmedNetherOreEntity extends Entity {

    public static final int DEFAULT_FUSE = 80;

    private int fuse = DEFAULT_FUSE;
    private BlockPos targetPos = BlockPos.ZERO;

    public ArmedNetherOreEntity(EntityType<? extends ArmedNetherOreEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.blocksBuilding = false;
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setInvulnerable(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // No synchronized data is needed; this entity is completely invisible.
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.fuse = tag.contains("Fuse") ? tag.getInt("Fuse") : DEFAULT_FUSE;
        if (tag.contains("TargetX") && tag.contains("TargetY") && tag.contains("TargetZ")) {
            this.targetPos = new BlockPos(tag.getInt("TargetX"), tag.getInt("TargetY"), tag.getInt("TargetZ"));
        } else {
            this.targetPos = this.blockPosition();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Fuse", this.fuse);
        tag.putInt("TargetX", this.targetPos.getX());
        tag.putInt("TargetY", this.targetPos.getY());
        tag.putInt("TargetZ", this.targetPos.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            return;
        }

        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }

        if (this.fuse > 0) {
            this.fuse--;
        }

        if (this.fuse <= 0) {
            this.explode();
            this.discard();
        }
    }

    private void explode() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        NetherOreMechanics.DifficultySettings settings = NetherOreMechanics.settingsFor(serverLevel.getDifficulty());
        if (settings.explosionPower() <= 0.0F) {
            return;
        }

        serverLevel.explode(this, this.getX(), this.getY(), this.getZ(), settings.explosionPower(),
                Level.ExplosionInteraction.TNT);
    }

    public BlockPos getTargetPos() {
        return this.targetPos;
    }

    public void setTargetPos(BlockPos targetPos) {
        this.targetPos = targetPos.immutable();
    }

    public int getFuse() {
        return this.fuse;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void kill() {
        // Keep admin commands such as /kill able to remove the timer entity.
        this.discard();
    }
}
