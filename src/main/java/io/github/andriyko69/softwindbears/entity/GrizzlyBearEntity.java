package io.github.andriyko69.softwindbears.entity;

import io.github.andriyko69.softwindbears.ai.GrizzlyBearFishGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class GrizzlyBearEntity extends AbstractBearEntity {

    public GrizzlyBearEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void addVariantGoals() {
        this.goalSelector.addGoal(5, new GrizzlyBearFishGoal(this, 1.0D, 20));
    }
}
