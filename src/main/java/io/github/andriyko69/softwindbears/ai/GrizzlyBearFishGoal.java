package io.github.andriyko69.softwindbears.ai;

import io.github.andriyko69.softwindbears.entity.GrizzlyBearEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;

public class GrizzlyBearFishGoal extends MoveToBlockGoal {
    private int upTick;
    private boolean fished;
    private int stayTick;

    public GrizzlyBearFishGoal(GrizzlyBearEntity mob, double speed, int range) {
        super(mob, speed, range, 4);
        this.upTick = mob.getRandom().nextInt(20) + 10;
        this.stayTick = mob.getRandom().nextInt(100) + 40;
    }

    @Override
    protected boolean isValidTarget(LevelReader world, @NotNull BlockPos pos) {
        return world.getBlockState(pos).is(Blocks.WATER) && world.isEmptyBlock(pos.above());
    }

    @Override
    protected int nextStartTick(@NotNull PathfinderMob mob) {
        return 1;
    }

    @Override
    public boolean canUse() {
        return !mob.isBaby() && mob.getRandom().nextInt(6500) == 1 && super.canUse();
    }

    @Override
    public void start() {
        this.upTick = mob.getRandom().nextInt(5) + 10;
        this.stayTick = mob.getRandom().nextInt(40) + 100;
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        if (fished) {
            fished = false;
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        if (this.isReachedTarget() && upTick <= 0) {
            ((GrizzlyBearEntity) mob).setStanding(false);

            if (!mob.level().isClientSide) {
                ServerLevel sl = (ServerLevel) mob.level();
                LootTable table = sl.getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);

                LootParams params = new LootParams.Builder(sl)
                        .withParameter(LootContextParams.ORIGIN, mob.position())
                        .withParameter(LootContextParams.THIS_ENTITY, mob)
                        .create(LootContextParamSets.COMMAND); // matches your original behavior

                for (ItemStack stack : table.getRandomItems(params)) {
                    if (!stack.is(ItemTags.FISHES) || mob.getRandom().nextInt(3) == 1) {
                        ItemEntity it = new ItemEntity(mob.level(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
                        double d = mob.getX() - blockPos.getX();
                        double e = mob.getY() - blockPos.getY();
                        double f = mob.getZ() - blockPos.getZ();
                        it.setDeltaMovement(d * 0.1D, e * 0.1D + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * 0.1D);
                        mob.level().addFreshEntity(it);
                        it.playSound(SoundEvents.ITEM_PICKUP, 0.2F, 2F);
                    } else {
                        mob.heal(2F);
                        for (int i = 0; i < 8; i++) {
                            double x = mob.getX() + blockPos.getX();
                            double y = 2.25 + mob.getY();
                            double z = blockPos.getZ() + mob.getZ();
                            sl.sendParticles(ParticleTypes.END_ROD, x / 2, y, z / 2, 1, 0, -0.2F, 0, 0.2F);
                        }
                    }
                }
            }

            fished = true;
        } else if (this.isReachedTarget()) {
            if (stayTick <= 0) {
                ((GrizzlyBearEntity) mob).setStanding(true);
                upTick--;
            } else {
                mob.getLookControl().setLookAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                stayTick--;
            }
        }

        super.tick();
    }
}
