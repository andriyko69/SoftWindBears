package io.github.andriyko69.softwindbears.entity;

import io.github.andriyko69.softwindbears.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PandaBearEntity extends AbstractBearEntity {

    public PandaBearEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected Ingredient getTemptItems() {
        return Ingredient.of(Items.BAMBOO);
    }

    @Override
    protected void addVariantGoals() {
    }

    @Override
    protected @Nullable EntityType<? extends AgeableMob> getOffspringType() {
        return ModEntities.BEAR_PANDA.get();
    }
}