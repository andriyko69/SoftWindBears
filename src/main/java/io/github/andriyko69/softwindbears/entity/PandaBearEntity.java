package io.github.andriyko69.softwindbears.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class PandaBearEntity extends AbstractBearEntity {

    public PandaBearEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void addVariantGoals() {
    }
}
