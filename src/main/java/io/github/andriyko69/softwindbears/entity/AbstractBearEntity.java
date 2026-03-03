package io.github.andriyko69.softwindbears.entity;

import io.github.andriyko69.softwindbears.Config;
import io.github.andriyko69.softwindbears.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractBearEntity extends Animal implements NeutralMob {
    private static final EntityDataAccessor<Boolean> WARNING =
            SynchedEntityData.defineId(AbstractBearEntity.class, EntityDataSerializers.BOOLEAN);

    private float lastWarningAnimationProgress;
    private float warningAnimationProgress;
    private int warningSoundCooldown;

    private int angerTime;
    private UUID targetUuid;

    private static final UniformInt ANGER_TIME_RANGE =
            TimeUtil.rangeOfSeconds(Config.bearAngerMinDuration, Config.bearAngerMaxDuration);

    protected AbstractBearEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    protected abstract Ingredient getTemptItems();

    protected abstract void addVariantGoals(); // panda vs grizzly goals

    protected abstract @Nullable EntityType<? extends AgeableMob> getOffspringType();

    protected boolean usesDefaultAttackGoals() {
        return true;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        boolean isFood = this.isFood(player.getItemInHand(hand));
        if (!isFood && !player.isSecondaryUseActive()) {
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new FloatGoal(this));

        if (usesDefaultAttackGoals()) {
            this.goalSelector.addGoal(1, new AttackGoal());
            this.goalSelector.addGoal(1, new BearEscapeDangerGoal());
        }

        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, getTemptItems(), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));

        addVariantGoals();

        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new BearRevengeGoal());

        if (!Config.areFriends) {
            this.targetSelector.addGoal(2, new ProtectBabiesGoal());
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
            this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Fox.class, 10, true, true, null));
            this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Rabbit.class, 10, true, true, null));
            this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Chicken.class, 10, true, true, null));
            this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Bee.class, 10, true, true, null));
            this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    protected void applyConfigAttributes() {
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(Config.bearHealth);
        Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).setBaseValue(Config.bearFollowRange);
        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(Config.bearSpeed);
        Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(Config.bearAttackDamage);

        double max = this.getMaxHealth();
        if (this.getHealth() > max) this.setHealth((float) max);
        if (this.tickCount < 5) this.setHealth((float) max);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.readPersistentAngerSaveData(this.level(), nbt);

        if (!this.level().isClientSide) {
            this.applyConfigAttributes();
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        this.addPersistentAngerSaveData(nbt);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return getTemptItems().test(stack);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob other) {
        EntityType<? extends AgeableMob> type = getOffspringType();
        return type == null ? null : type.create(level);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int ticks) {
        this.angerTime = ticks;
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return this.targetUuid;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.targetUuid = uuid;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? ModSounds.BEAR_AMBIENT_BABY.get() : ModSounds.BEAR_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.BEAR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.BEAR_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(ModSounds.BEAR_STEP.get(), 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundCooldown <= 0) {
            this.playSound(ModSounds.BEAR_WARNING.get(), 1.0F, this.getVoicePitch());
            this.warningSoundCooldown = 40;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(WARNING, false);
        super.defineSynchedData(builder);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            if (this.warningAnimationProgress != this.lastWarningAnimationProgress) {
                this.refreshDimensions();
            }

            this.lastWarningAnimationProgress = this.warningAnimationProgress;

            if (this.isStanding()) {
                this.warningAnimationProgress = Mth.clamp(this.warningAnimationProgress + 1.0F, 0.0F, 6.0F);
            } else {
                this.warningAnimationProgress = Mth.clamp(this.warningAnimationProgress - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundCooldown > 0) {
            --this.warningSoundCooldown;
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }
    }

    @Override
    public @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        if (this.warningAnimationProgress > 0.0F) {
            float f = this.warningAnimationProgress / 6.0F;
            float g = 1.0F + f;
            return super.getDefaultDimensions(pose).scale(1.0F, g);
        }
        return super.getDefaultDimensions(pose);
    }

    public boolean isStanding() {
        return this.entityData.get(WARNING);
    }

    public void setStanding(boolean warning) {
        this.entityData.set(WARNING, warning);
    }

    public float getStandingAnimationScale(float tickDelta) {
        return Mth.lerp(tickDelta, this.lastWarningAnimationProgress, this.warningAnimationProgress) / 6.0F;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level,
                                                 @NotNull DifficultyInstance difficulty,
                                                 @NotNull MobSpawnType spawnType,
                                                 @Nullable SpawnGroupData spawnGroupData) {
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(1.0F);
        }

        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);

        if (!this.level().isClientSide) {
            this.applyConfigAttributes();
        }

        return data;
    }

    class BearEscapeDangerGoal extends PanicGoal {
        public BearEscapeDangerGoal() {
            super(AbstractBearEntity.this, 2.0D);
        }

        @Override
        public boolean canUse() {
            return (AbstractBearEntity.this.isBaby() || AbstractBearEntity.this.isOnFire()) && super.canUse();
        }
    }

    private class AttackGoal extends MeleeAttackGoal {
        public AttackGoal() {
            super(AbstractBearEntity.this, 1.25D, true);
        }

        @Override
        protected void checkAndPerformAttack(@NotNull LivingEntity target) {
            double d = this.getAttackReachSqr(target);
            double squaredDistance = target.distanceToSqr(this.mob);

            if (squaredDistance <= d && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(target);
                AbstractBearEntity.this.setStanding(false);
            } else if (squaredDistance <= d * 2.0D) {
                if (this.isTimeToAttack()) {
                    AbstractBearEntity.this.setStanding(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    AbstractBearEntity.this.setStanding(true);
                    AbstractBearEntity.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                AbstractBearEntity.this.setStanding(false);
            }
        }

        @Override
        public void stop() {
            AbstractBearEntity.this.setStanding(false);
            super.stop();
        }

        protected double getAttackReachSqr(LivingEntity entity) {
            return 4.0F + entity.getBbWidth();
        }
    }

    class BearRevengeGoal extends HurtByTargetGoal {
        public BearRevengeGoal() {
            super(AbstractBearEntity.this);
        }

        @Override
        public void start() {
            super.start();
            if (AbstractBearEntity.this.isBaby()) {
                this.alertOthers();
                this.stop();
            }
        }

        @Override
        protected void alertOther(@NotNull Mob mob, @NotNull LivingEntity target) {
            if (mob instanceof AbstractBearEntity && !mob.isBaby()) {
                super.alertOther(mob, target);
            }
        }
    }

    class ProtectBabiesGoal extends NearestAttackableTargetGoal<Player> {
        public ProtectBabiesGoal() {
            super(AbstractBearEntity.this, Player.class, 20, true, true, null);
        }

        @Override
        public boolean canUse() {
            if (!AbstractBearEntity.this.isBaby() && super.canUse()) {
                List<AbstractBearEntity> list = AbstractBearEntity.this.level().getEntitiesOfClass(
                        AbstractBearEntity.class,
                        AbstractBearEntity.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D)
                );
                for (AbstractBearEntity bear : list) {
                    if (bear.isBaby()) return true;
                }
            }
            return false;
        }

        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }
}