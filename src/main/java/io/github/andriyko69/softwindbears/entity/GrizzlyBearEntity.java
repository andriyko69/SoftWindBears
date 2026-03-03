package io.github.andriyko69.softwindbears.entity;

import io.github.andriyko69.softwindbears.ai.GrizzlyBearFishGoal;
import io.github.andriyko69.softwindbears.registry.ModEntities;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GrizzlyBearEntity extends AbstractBearEntity {

    public GrizzlyBearEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected Ingredient getTemptItems() {
        return Ingredient.of(Items.SWEET_BERRIES);
    }

    @Override
    protected void addVariantGoals() {
        this.goalSelector.addGoal(5, new GrizzlyBearFishGoal(this, 1.0D, 20));
    }

    @Override
    protected @Nullable EntityType<? extends AgeableMob> getOffspringType() {
        return ModEntities.GRIZZLY_BEAR.get();
    }
}